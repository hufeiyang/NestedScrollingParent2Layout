
# 概要：
滑动嵌套通用解决方案：提供常见三种RecyclerView滑动嵌套布局、也可自定义。

详细见我的blog：
https://blog.csdn.net/hfy8971613/article/details/105331221

# 依赖

> implementation 'com.hfy.nestedscrollinglayout:nestedscrollinglayout:1.0.1'


# 使用说明
1、 NestedScrollingParent2LayoutImpl1
使用场景：处理 header + tab + viewPager + recyclerView，且保证 recyclerView复用机制不失效

2、NestedScrollingParent2LayoutImpl2
使用场景：处理 header + recyclerView 的 嵌套滑动，且保证 recyclerView复用机制不失效

![在这里插入图片描述](https://img-blog.csdnimg.cn/202004052230284.gif#pic_center)

3、 NestedScrollingParent2LayoutImpl3
使用场景：处理RecyclerView 套viewPager， viewPager内的fragment中 也有RecyclerView，处理外层、内层 RecyclerView的嵌套滑动问题，类似淘宝、京东首页。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200405223817325.gif#pic_center)


4、 NestedScrollingParent2Layout
使用场景：可继承此layout写自己的滑动逻辑即可。


## 关于我
[我的CSDN](https://blog.csdn.net/hfy8971613?t=1)

公众号：胡飞洋
![公众号：胡飞洋](https://img-blog.csdnimg.cn/20200406113801806.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hmeTg5NzE2MTM=,size_16,color_FFFFFF,t_70#pic_center)
