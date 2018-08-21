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
    func(1, 2, 3);
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

fn main() {
    //genericTest();
    //concatTest();
    //functionRenameTest();
    //twoArgsTest();
    //returnTest();

    eqTest();
}