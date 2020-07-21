package com.example.myapplication.test



fun main(){

    val trans = object :Transformer<Person>{
        override fun transform(t: Person): String {
            return "${t.name} ${t.age}"
        }
    }
    // 此时，Transformer<Person> 已经是 Transformer<Student> 的子类型了
    handleTransformer(trans)

    // 如果让泛型 T 出现在 out 位置的隐患
    val trans1 = object :Transformer1<Person>{
        // 构建了一个 Teacher 对象，并直接返回。
        override fun transform(name: String,age: Int): Person {
            // transform() 的返回值要求是一个 Person 对象，而 Teacher 是 Person 的子类，这种写法是合法的。
            return Teacher(name,age)
        }
    }
    handleTransformer1(trans1)
}

fun handleTransformer(trans:Transformer<Student>){
    val student = Student("Tom",19)
    val result = trans.transform(student)
    println(result)
    // 打印结果:
    // Tom 19
}

fun handleTransformer1(trans:Transformer1<Student>){
    // 期望得到的是一个 Student 对象，但实际上得到的是一个 Teacher 对象，因此造成类型转换异常。
    val result = trans.transform("Tom",19)
    println(result)
    // 打印结果
    // Exception in thread "main" java.lang.ClassCastException:
    // com.example.myapplication.test.Teacher cannot be cast to com.example.myapplication.test.Student
}

/**
 * 用来执行一些转换操作
 * in 关键字表示 T 只能出现在 in 位置，Transformer 在泛型 T 上是逆变的
 */
interface Transformer<in T>{
    fun transform(t:T):String
}


/**
 * 如果让泛型 T 出现在 out 位置的隐患
 */
interface Transformer1<in T>{
    fun transform(name:String,age: Int):@UnsafeVariance T
}

open class Person(val name: String,val age:Int)
class Student(name:String,age: Int):Person(name,age)
class Teacher(name:String,age: Int):Person(name,age)