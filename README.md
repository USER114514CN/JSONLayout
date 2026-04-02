**代码还是alpha版本 你要是不怕出bug你就用吧**

# JSONLayout
轻量级在Java中通过JSON源文件/JSON字符串初始化布局，肥肠牛逼克拉斯😋

让我想一想...
如果这个库突然一夜爆火，无人不知，甚至圈外人都知道的话，我USER114514就能走上人生巅峰😋😋😋
哎呀又做白日梦……

## 示例JSON
```json
{
    "rootLayout": {
        "layout": {
            "class": "java.awt.FlowLayout",
            "align": 1,
            "hgap": 10,
            "vgap": 10
        },
        "size": {
            "class": "java.awt.Dimension",
            "width": 400,
            "height": 300
        },
        "title": "Test Window",
        "location": "center",
        "contents": [
            {
                "class": "java.awt.Label",
                "text": "Hello, World!"
            },
            {
                "class": "java.awt.Button",
                "text": "Click Me!"
            }
        ]
    }
}
```

## JSONConvertable注解示例
```java
@JSONConvertable
public class MyComponent { 
    private String text;
    private int size;
    public MyComponent() {
        // 默认构造器
    }
    @SetterInvokable(name = "text")
    public void setText(String text) {
        this.text = text;
    }
    @SetterInvokable(name = "size")
    public void setSize(int size) {
        this.size = size;
    }

```

***关于JSONConvertabe注解的注意事项***

1.被标记JSONConvertable的类必须有一个无参public构造器。

2.被标记SetterInvokable的方法必须为public，只有一个参数。
