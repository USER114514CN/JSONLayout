import com.user114514.jsonlayout.core.*;

import java.awt.*;

public class Test {
    public static void main(String[] args) throws Exception {
        Frame windowFrame = new Frame();

        String json = """
        {
            "rootLayout": {
                "title": "Panel contents 测试",
                "size": {
                    "class": "java.awt.Dimension",
                    "width": 500,
                    "height": 400
                },
                "location": "center",
                "layout": {
                    "class": "java.awt.FlowLayout"
                },
                "contents": [
                    {
                        "class": "java.awt.Panel",
                        "background": {
                            "class": "java.awt.Color",
                            "red": 200,
                            "green": 200,
                            "blue": 255
                        },
                        "contents": [
                            {
                                "class": "java.awt.Label",
                                "text": "我在 Panel 里面！"
                            },
                            {
                                "class": "java.awt.Button",
                                "label": "Panel按钮1"
                            },
                            {
                                "class": "java.awt.Button",
                                "label": "Panel按钮2"
                            }
                        ]
                    },
                    {
                        "class": "java.awt.Label",
                        "text": "我在外面，不在Panel里"
                    }
                ]
            }
        }
        """;
        
        LayoutLoader loader = new LayoutLoader(windowFrame);
        loader.loadLayout(json);

        windowFrame.setVisible(true);
        // GitHub 仓库地址：https://github.com/USER114514CN/JSONLayout/

        // 一定要成功！！！！！！！！！！！！！！！
        // 这段代码是用来测试 LayoutLoader 的，看看它能不能正确地解析 JSON 配置并创建一个窗口，窗口里有一个标签和一个按钮，布局是 FlowLayout，窗口标题是 "Test Window"，大小是 400x300，位置在屏幕中央。
        // 成败就在这一瞬间了！！！！！！！！！！！！！！！

        // 当我那颤抖的手按下Ctrl+F5的那一刻，整个世界都安静了下来，仿佛时间都凝固了，我的心跳声在耳边回响，屏幕上开始加载那个窗口，标签和按钮慢慢地显现出来，我的眼睛里充满了泪水，因为我知道，这不仅仅是一个窗口，更是我无数个日日夜夜努力的结晶，是我对编程的热爱和坚持的证明，是我对自己能力的肯定和自信的来源，是我对未来无限可能的期待和憧憬！！！！！！！！！！！！！！
        // 哈哈骗你的，他说我类型不匹配，翻车了，哭死了😭😭😭

        // 现在又是 NoSuchMethodException，哭死了😭😭😭
        // Button 没有 setText 方法，Label 也没有 setText 方法，哈哈哈哈哈，居然把这个细节忘了，哭死了😭😭😭 (后续证明不是的)
        // 这咋办...我这个代码对于settable是非黑即白的，要么全用setter，要么全用构造器参数，不能混用
        // 我得重新设计一下这个系统，让它更灵活一点
        // 但是我现在太累了，先放一放吧，等我休息好了再说
        // 嗯嗯我是大懒虫
        // 我要玩会Minecraft了，哈哈哈哈哈，反正这个项目也不是很急，慢慢来吧，先把这个窗口弄出来再说，其他的功能以后再加上去就好了，不着急不着急，慢慢来慢慢来，哈哈哈哈哈
        // GitHub的README.md我已经写了我还在调试，不慌不慌，慢慢来慢慢来，哈哈哈哈哈
        // 哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈
        // 还有GitHub Copilot也是非常牛逼克拉斯的，attribute-map.json我就是让它帮我写的，哈哈哈哈哈，虽然它写的代码有点冗余和不够优雅，但是总的来说还是很不错的，至少比我自己写的要好很多了，哈哈哈哈哈，感谢GitHub Copilot，你是我的好帮手，我爱你❤️❤️❤️
        // OS: 有点肉麻了吧...
        // 咳咳，不过Java AWT字体这一块拉完了，连中文都显示不了，我他妈还不能手动setFont，你也知道的Font就那几个预设("Dialog", "Serif", "SansSerif", "Monospaced")，都不支持中文，createFont使用TTF文件还他妈渲染不了，哭死了😭😭😭
        // 我写这么一大堆注释感觉是疯了……
        // 哈哈哈哈哈哈哈哈哈哈
        // 哈你妈，哈哈就能解决问题吗，我真是个大傻子，哈哈哈哈哈，哭死了😭😭😭
        // 照这样下去整个项目50MB结果49MB都是注释了，哈哈哈哈哈
        // 还是交给未来的我

        // 气死我了，玩Minecraft PVP结果有人开挂，挂狗太坏了😡😡😡😡
        // 开挂的都傻子，哈哈哈哈哈，开挂的都没朋友，哈哈哈哈哈，开挂的都没自信，哈哈哈哈哈，开挂的都没尊严，哈哈哈哈哈，开挂的都没未来，哈哈哈哈哈，开挂的都没意义，哈哈哈哈哈，开挂的都没价值，哈哈哈哈哈，开挂的都没快乐，哈哈哈哈哈，开挂的都没成就感，哈哈哈哈哈，开挂的都没满足感，哈哈哈哈哈，开挂的都没幸福感，哈哈哈哈哈，开挂的都没自我实现感，哈哈哈哈哈，开挂的都没自我超越感，哈哈哈哈哈，开挂的都没自我完善感，哈哈哈哈哈，开挂的都没自我成长感，哈哈哈哈哈，开挂的都没自我发展感，哈哈哈哈哈，开挂的都没自我实现感，哈哈哈哈哈，开挂的都没自我超越感，哈哈哈哈哈，开挂的都没自我完善感，哈哈哈哈哈，开挂的都没自我成长感，哈哈哈哈哈，开挂的都没自我发展感，哈哈哈哈哈，开挂...
        // 以后我遇到开挂的见一个肘击一个

        // Label 有 setText 方法😋😋😋
        // 我去！原来 Button 有 setLabel 方法啊，不用重构代码了🙄🙄🙄

        // 记于 2026年4月2日18:28:23
        // 成成成成成成成成成成成成成成成功了！！！！！！！！！！！！！！
        // 我有出息了！！！！！！！！！！！！！！
        // 俺不是孬种🤗🤗🤗

        // 又来了一把 Minecraft Bedwars，结果又遇到堵出生点的了，气死我了😡😡😡😡

        // 2026年4月3日17:44:56
        // 我刚刚加入了Panel的contents属性，不知道能不能成……
        // Exception in thread "main" java.lang.RuntimeException: java.lang.NoSuchMethodException: java.awt.Panel.setBackground(java.lang.String)
        // ……
        // 豆包你个傻子，Panel的setBackground方法需要一个Color对象，而不是一个字符串，你还想让我怎么处理这个属性啊，你这是在搞事吗？我已急哭🤗🤗🤗🤗
        // Exception in thread "main" java.lang.RuntimeException: java.lang.NoSuchMethodException: java.awt.Panel.setBackground(java.awt.Color)
        // 这有点离谱了，怎么可能会这样，他说java.lang.NoSuchMethodException: java.awt.Panel.setBackground(java.awt.Color)，但是不是有setBackground方法，参数也是Color啊...... 
        // 我问问 Github Copilot
        // Copilot: 你确定你传入的参数是java.awt.Color类型吗？你可以在调用setBackground方法之前打印一下参数的类型，看看是不是Color对象。
        // 我一直用的是java.awt.Color啊，他还告诉我没有java.awt.Panel.setBackground(java.awt.Color)这个方法，不可能啊，Java这是左右脑互搏吗？
        // 这有点离谱了，怎么可能会这样，他说java.lang.NoSuchMethodException: java.awt.Panel.setBackground(java.awt.Color)，但是不是有setBackground方法，参数也是Color啊......
        // 我问问 Github Copilot
        // Copilot: 既然你确定你传入的参数是java.awt.Color类型，那么可能是因为你在调用setBackground方法之前没有正确地导入java.awt.Color类，或者你的代码中有多个Color类导致了命名冲突。你可以检查一下你的import语句，确保你导入的是java.awt.Color类，并且在使用Color对象时使用全限定名来避免命名冲突。
        // 导入了java.awt.Color了啊，命名冲突也没有啊，我的代码里只有一个Color类啊，Java这是在他妈的整我吗😱😱😱😱😱😱😱😱😱😱😡🥴🥴🥵🥵🥵😭😭😭😭😭
        // 你妈setBackground方法是Panel的父类Component的方法啊，getDeclaredMethod只能获取Panel自己声明的方法，不能获取父类的方法啊，啥子嘛，好吧，我要把getDeclaredMethod改成getMethod了，看看能不能解决问题，Java我***你妈啊
        // 我用 Ctrl+H 替换了 getDeclaredMethod 为 getMethod
        // 成功了！！！！！！！！！！！！！！
    }
}
