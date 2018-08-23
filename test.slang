var d = a * 32;
var a = 123 * 2;

fn genericTest() {
    println("d: ", d);
    var b = a + 321;
    println("Hello, world! ", b);
    b = 1337 / 2.1;
    println("b: ", b);
}

fn concatTest() {
    var a = "Test " + "thing";
    var b = "Test + " + 123;
    var c = 321 + " Test";
    println(a);
    println(b);
    println(c);
}

fn functionRenameTest() {
    var myprint = println;
    myprint("Lul!");
}

fn twoArgsTest() {
    fn func(a, b) {
        println("I got ", a, " and ", b, "!");
    }
    func(1, 2);
    //func(1, 2, 3); // this should fail
}

fn eqTest() {
    if null {
        println("yay!");
    } else {
        println("nay!");
    }

}

fn returnTest() {
    fn sqrt(n) {
        return n * n;
    }

    println("sqrt(4): ", sqrt(4));
}

var recurseCount = 100;
fn recurse() {
    if recurseCount <= 0 do return;
    println("recurseCount = ", recurseCount);
    recurseCount -= 1;
    recurse();
}

fn assignmentOperators() {
    var a = 321;
    a -= 22;
    a += 11;
    println(a);
}

fn compTest() {
    var testa = 1;
    if testa == 1 {
        println("Yes");
    }
    testa += 2;
    if testa != 1 {
        println("Yes 2x");
    }

    var test = true;
    if test == true {
        println("Yes 3x");
    }
    if !test == false {
        println("Yes 4x");
    }

    var isnul = null;
    var isnotnul = "String";

    if isnul == null {
        println("Yea 5x");
    }
    if isnotnul != null {
        println("Yea 6x");
    }

    if isnul != isnotnul {
        println("Yea 7x");
    }
}

fn factorialTest() {
    fn factorial(n) {
        if n <= 1 do return 1;
        else do return n * factorial(n-1);
    }
    println("factorial(5) = ", factorial(5));
}

fn arrayTest() {
    var a = [1, 2, 3, 4];
    a[2] += a[1] * 3;
    println("a[2] = ", a[2]);

    //a[100] = 1001;
    //println("a[100] = ", a[100]);

    println("len(a) = ", len(a));
    println("a = ", a);
}

fn lenTest() {
    println("len('Hello, world!') = ", len("Hello, world!"));
    println("len(1) = ", len(1));
    println("len([1, 2, 3, 4, 5]) = ", len([1, 2, 3, 4, 5]));
}

var bigLoopCounter = 0;
fn bigLoopTest() {
    if bigLoopCounter == 10000 do return;
    println("bigLoop: ", bigLoopCounter);
    bigLoopCounter += 1;
    bigLoopTest();
}

fn mathTest() {
    println("(2) + (17*2-30) * (5)+2 - (8/2)*4 should be 8 we get ", (2) + (17*2-30) * (5)+2 - (8/2)*4);
    println("'2*3*4/8 -   5/2*4 +  6 + 0/3  ' should be -1 we get ", 2*3*4/8 -   5/2*4 +  6 + 0/3   );
    println("-(2) = ", -(2));
    println("1 + eps = ", 1 + eps);
}

fn tableTest() {
    var t = {
        ["hello"] = "world!",
        //["123test"] = "Hello, ",
        [0] = "Test",
        [123] = 321
    };
    println("t = ", t);
    t[0] = "Test!";
    var helloV = "hello";
    println("t[0] = ", t[0]);
    println("t[123] = ", t[123]);

    //t["Lol"] = 123;
    t[0] = 321;

    println("len(t) = ", len(t));
}

fn whileTest() {
    var i = 0;
    while i < 100000 {
        i += 1;
        if i % 10000 == 0 {
            println("i = ", i);
        }
    }
    println("Yay done!");
}

fn utf8Test() {
    var 汉 = 123;
    println("汉 = ",  汉);
}

fn anonFuncTest() {
    var myprint = fn(a) {
        println(a);
    };
    myprint("Anon print works");


    var test = fn(msg) { return "Amazeballs " + msg; }("are awesome!");
    println(test);

    // We could get this to work if we really wanted, but its not really needed. Like this entire language... :(
    //fn(a, b) {
    //    println("a + b = ", a + b);
    //}(1, 2);
}

//fn printf(fmt, *args) {
//    println(fmt, ..args); // A 'spread' operator would be nice
//}

fn main() {
    if true {
        genericTest();
        concatTest();
        functionRenameTest();
        twoArgsTest();
        returnTest();
        recurse();
        assignmentOperators();
        compTest();
        eqTest();
        lenTest();
        //bigLoopTest();
        mathTest();
        arrayTest();
        tableTest();
        utf8Test();

        factorialTest();
        whileTest();
    }

    anonFuncTest();

    return 123;
}