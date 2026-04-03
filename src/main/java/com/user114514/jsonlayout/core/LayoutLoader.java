// 再也不不写注释了

//这应该算是alpha版本吧


package com.user114514.jsonlayout.core;

import org.json.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LayoutLoader {
    // 这个类是整个库的核心，负责解析 JSON 配置并构建 GUI 布局

    private Frame mRootFrame;
    private ClassLoader mClassLoader;
    private JSONObject mAttributeMap;
    
    public LayoutLoader(Frame rootFrame) {
        this.mRootFrame = rootFrame;
        this.mClassLoader = this.getClass().getClassLoader();

        try (InputStream is = this.mClassLoader.getResourceAsStream("attribute-map.json")) {
            if (is == null) {
                throw new FileNotFoundException("Resource 'attribute-map.json' not found");
            }
            String jsonString = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            this.mAttributeMap = new JSONObject(jsonString);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Attribute map file not found", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load attribute map", e);
        }
    }

    public void loadLayout(String jsonString) throws ClassNotFoundException {
        JSONObject json = new JSONObject(jsonString);
        loadLayout(json);
    }

    public void loadLayout(JSONObject json) throws ClassNotFoundException {
        JSONObject layoutObject = json.optJSONObject("rootLayout");
        for (String key : layoutObject.keySet()) {
            switch (key) {
                case "layout":
                    JSONObject layoutJsonObject = layoutObject.optJSONObject(key);
                    Class<?> layoutClass = Class.forName(layoutJsonObject.optString("class"));
                    this.mRootFrame.setLayout((LayoutManager) loadObject(layoutClass, layoutJsonObject));
                    break;
                case "contents":
                    JSONArray contentsArray = layoutObject.optJSONArray(key);
                    for (Object element : contentsArray) {
                        JSONObject contentObject = (JSONObject) element;
                        Class<?> contentsLayoutClass = Class.forName(contentObject.optString("class"));
                        this.mRootFrame.add((Component) loadObject(contentsLayoutClass, contentObject));
                    }
                    break;
                case "title":
                    this.mRootFrame.setTitle(layoutObject.optString(key));
                    break;
                case "size":
                    // 或许这里不需要判断了，毕竟 size 只能是 Dimension 对象
                    this.mRootFrame.setSize(loadObject(Dimension.class, layoutObject.optJSONObject(key)));
                    break;
                case "location":
                    // 原因同上，location 只能是 Point 对象或者 "center" 字符串
                    Object locationValue = layoutObject.opt(key);
                    if (locationValue instanceof JSONObject) {
                        this.mRootFrame.setLocation(loadObject(Point.class, (JSONObject) locationValue));
                    } else if (locationValue instanceof String && ((String) locationValue).equalsIgnoreCase("center")) {
                        this.mRootFrame.setLocationRelativeTo(null);
                    } else {
                        throw new RuntimeException("Invalid location value: " + locationValue);
                    }
                    break;
            }
        }
    }

    private <T> T loadObject(Class<T> clazz, JSONObject object) {
        if (object == null) throw new RuntimeException("JSONObject is null.");
        if (!clazz.getName().equals(object.optString("class")))
            throw new RuntimeException("Class mismatch.");
        object.remove("class"); // 这个属性已经没什么意义了，反正我们已经拿到 Class 对象了，如果不 remove 掉的话后面还会被当成一个属性来处理，疯狂抛异常，然后我就急哭了。
        // if (!this.mAttributeMap.has(clazz.getName())) throw new RuntimeException("Not found class attribute map.");
        // 我才发现我上面那一行代码让 JSONConvertable 变成摆设
        if (!this.mAttributeMap.has(clazz.getName())) {
            if (!clazz.isAnnotationPresent(JSONConvertable.class)) {
                // 你连这个类都没有标记成 JSONConvertable，你还想让我帮你解析？
                // 回去再把README.md好好看看吧
                throw new RuntimeException("Class '" + clazz.getName() + "' is not JSONConvertable.");
            }
            Map<String, Method> setterMap = new HashMap<>();
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(SetterInvokable.class)) {
                    SetterInvokable annotation = method.getAnnotation(SetterInvokable.class);
                    setterMap.put(annotation.name(), method);
                }
            }
            T instance;
            Constructor<T> constructor;
            try {
                constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                instance = constructor.newInstance();
            } catch (NoSuchMethodException e) {
                // 你都没有无参构造器了还想让我帮你解析？回去再把README.md好好看看吧
                throw new RuntimeException("Class '" + clazz.getName() + "' does not have a no-argument constructor.");
                // 我要疯狂抛异常烦死你😈😈😈
            } catch (SecurityException e) {
                // 你都没有无参构造器了还想让我帮你解析？回去再把README.md好好看看吧
                throw new RuntimeException("Class '" + clazz.getName() + "' does not have a no-argument constructor.");
                // 我要疯狂抛异常烦死你😈😈😈
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate class '" + clazz.getName() + "'.", e);
            }
            

            for (String keyName : object.keySet()) {
                Object value = object.opt(keyName);
                if (!setterMap.containsKey(keyName)) throw new UnknownAttributeException("Unknow attribute name: " + keyName);
                Method method = setterMap.get(keyName);
                if (method.getParameterCount() != 1) throw new RuntimeException("Setter method must have exactly one parameter: " + method.getName());
                Class<?> paramClass = (object.opt(keyName).getClass().equals(JSONObject.class) ? method.getParameterTypes()[0] : object.opt(keyName).getClass());
                if (!(value.getClass().equals(JSONObject.class) ? object.optJSONObject(keyName).optString("class") : value.getClass().getName()).equals(paramClass.getName())){
                    // 不匹配就是不匹配哪来那么多废话
                    throw new RuntimeException("Type mismatch for attribute '" + keyName + "': expected " + paramClass.getName() + " but got " + (object.opt(keyName).getClass().equals(JSONObject.class) ? object.optJSONObject(keyName).optString("class") : object.opt(keyName).getClass().getName()));
                }
                method.setAccessible(true);
                try {
                    method.invoke(instance, (object.opt(keyName).getClass().equals(JSONObject.class) ? loadObject(paramClass, object.optJSONObject(keyName)) : object.opt(keyName)));
                } catch (IllegalAccessException e) {
                    // 666还不让我访问
                    throw new RuntimeException("Failed to invoke setter method '" + method.getName() + "' for attribute '" + keyName + "'.", e);
                } catch (InvocationTargetException e) {
                    // 666方法内部自己抛异常了
                    // 我把根本原因包装起来抛出，反正我也不想让用户看到那么多反射相关的异常了，还不快谢谢我
                    throw new RuntimeException("Failed to invoke setter method '" + method.getName() + "' for attribute '" + keyName + "'.", e.getCause());
                }
            }
            return instance;
        }
        JSONObject classObject = this.mAttributeMap.getJSONObject(clazz.getName());
        boolean settable = classObject.optBoolean("settable");
        if (!settable) {
            Set<String> keySet = object.keySet();
            JSONArray paramsGroupsArray = classObject.optJSONArray("paramsGroups");
            for (Object element : paramsGroupsArray) {
                JSONObject paramsGroupJsonObject = (JSONObject) element;
                JSONArray paramsNamesArray = paramsGroupJsonObject.optJSONArray("params-names");
                if (compareJSONArrayAndSet(paramsNamesArray, keySet)) {
                    Class<?>[] paramsClasses = new Class[paramsNamesArray.length()];
                    JSONArray constructorParamsTypes = paramsGroupJsonObject.optJSONArray("constructor-params-types");
                    Object[] values = new Object[paramsNamesArray.length()];
                    for (int i = 0; i < constructorParamsTypes.length(); i++) {
                        String typeString = constructorParamsTypes.optString(i);
                        try {
                            paramsClasses[i] = convertStringToClass(typeString);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException("Unknow class: " + e.getMessage());
                        }

                        Object currentValue = object.opt(paramsNamesArray.optString(i));
                        values[i] = (currentValue.getClass().equals(JSONObject.class) ? loadObject(paramsClasses[i], (JSONObject) currentValue) : currentValue);
                        if (values[i] == null) throw new RuntimeException("You must write the attribute '" + paramsNamesArray.optString(i) + "'.");
                    }
                    try {
                        Constructor<T> constructor = clazz.getDeclaredConstructor(paramsClasses);
                        constructor.setAccessible(true);
                        return constructor.newInstance(values);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    break;
                }
                
            }
            throw new UnknownAttributeException("Unknown attribute names: " + keySet.toString());
        } else {
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                T instance = constructor.newInstance();
                for (String keyName : object.keySet()) {
                    if (keyName.equals("contents")) continue; // contents 需要单独处理，不能当成一个属性来处理，不然就炸了
                    if (!classObject.has(keyName)) throw new UnknownAttributeException("Unknow attribute name: " + keyName);
                    String methodName = classObject.optString(keyName);
                    Class<?> paramClass = object.opt(keyName).getClass();
                    boolean isUnhandled = paramClass.equals(JSONObject.class);
                    Class<?> realClass = (isUnhandled ? Class.forName(object.optJSONObject(keyName).optString("class")) : paramClass);
                    Method method = clazz.getMethod(methodName, realClass);
                    method.setAccessible(true);
                    method.invoke(instance, (isUnhandled ? loadObject(realClass, object.optJSONObject(keyName)) : object.opt(keyName)));
                }
                // 这边我要单独处理 Panel 的 contents 属性，因为它需要调用 add 方法而不是 set 方法，高斯林和甲骨文你们这是在搞事吗？我已急哭
                if (clazz.equals(Panel.class) && object.has("contents")) {
                    JSONArray contentsArray = object.optJSONArray("contents");
                    for (Object element : contentsArray) {
                        JSONObject contentObject = (JSONObject) element;
                        Class<?> contentsLayoutClass = Class.forName(contentObject.optString("class"));
                        Method addMethod = clazz.getMethod("add", Component.class);
                        addMethod.setAccessible(true);
                        addMethod.invoke(instance, loadObject(contentsLayoutClass, contentObject));
                    }
                }
                return instance;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean compareJSONArrayAndSet(JSONArray array, Set<String> keySet) {
        if (array.length() != keySet.size()) return false;
        for (Object obj : array) {
            if (!keySet.contains(obj)) return false;
        }
        return true;
    }

    private Class<?> convertStringToClass(String name) throws ClassNotFoundException {
        switch (name) {
            case "int":
                return int.class;
            case "boolean":
                return boolean.class;
            case "float":
                return float.class;
            case "String":
            case "string":
                return String.class;
            default:
                return Class.forName(name);
        }
    }

}

// 我有点后悔不加注释了
// 现在我全都看不懂😭😭😭

// 傻子豆包把我的注释改的一点人情味都没有了，害我都不想写了😭😭😭
// 信不信我以后把我的主力 AI 换成 DeepSeek😈😈😈

// 哎不是气死我了 JSON 用不了注释，啥意思啊，乐子吗，狗屎一枚
