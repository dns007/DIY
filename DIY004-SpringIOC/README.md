 手写一个简单的SpringIOC（基于注解方式）
 
 流程：
 
1 首先需要实现类似于spring的componetscan:自动包扫描，获取对应的注解类
        
2 遍历注解的类,利用反射实例化（class.newInstance）注解类 放入 beanMap
        
3 对应的依赖属性初始化:主要通过反射+已经实例化好的bean集合