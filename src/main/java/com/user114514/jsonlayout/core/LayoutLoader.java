package com.user114514.jsonlayout.core;

import org.json.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class LayoutLoader {

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

    public void loadLayout(JSONObject json) {
        JSONObject layoutObject = json.optJSONObject("rootLayout");
        for (String key : layoutObject.keySet()) {
            switch (key) {
                case "layout":
                    this.mRootFrame.setLayout((LayoutManager) loadObject(LayoutManager.class, layoutObject.optJSONObject(key)));
                    break;
                case "contents":
                    JSONArray contentsArray = layoutObject.optJSONArray(key);
                    for (Object element : contentsArray) {
                        JSONObject contentObject = (JSONObject) element;
                        this.mRootFrame.add(loadObject(Component.class, contentObject));
                    }
                    break;
                case "title":
                    this.mRootFrame.setTitle(layoutObject.optString(key));
                    break;
                case "size":
                    this.mRootFrame.setSize(loadObject(Dimension.class, layoutObject.optJSONObject(key)));
                    break;
                case "location":
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
        if (!this.mAttributeMap.has(clazz.getName())) throw new RuntimeException("Not found class attribute map.");
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
                    if (!classObject.has(keyName)) throw new UnknownAttributeException("Unknow attribute name: " + keyName);
                    String methodName = classObject.optString(keyName);
                    Class<?> paramClass = object.opt(keyName).getClass();
                    boolean isUnhandled = paramClass.equals(JSONObject.class);
                    Class<?> realClass = (isUnhandled ? Class.forName(object.optJSONObject(keyName).optString("class")) : paramClass);
                    Method method = clazz.getDeclaredMethod(methodName, realClass);
                    method.invoke(instance, (isUnhandled ? loadObject(realClass, object.optJSONObject(keyName)) : object.opt(keyName)));
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
