fn Point(x, y) do return {
    x = x,
    y = y,

    add = fn(self, other) {
        self.x = self.x + other.x;
        self.y = self.y + other.y;
        return self;
    }
};

fn main() {
    var a = Point(1, 2);
    var b = Point(2, 3);
    a:add(b);
    println(a);
}