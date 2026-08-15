    public static Object nan = Double.NaN;
    public static Object isNaN = (java.util.function.Function<Object, Object>) (n) -> Double.isNaN((Double) n);
    public static Object infinity = Double.POSITIVE_INFINITY;
    public static Object isFinite = (java.util.function.Function<Object, Object>) (n) -> Double.isFinite((Double) n);

    public static Object fromStringImpl = (java.util.function.Function<Object, Object>) (str) -> (java.util.function.Function<Object, Object>) (isFiniteF) -> (java.util.function.Function<Object, Object>) (just) -> (java.util.function.Function<Object, Object>) (nothing) -> {
        try {
            double num = Double.parseDouble((String) str);
            if ((Boolean) ((java.util.function.Function<Object, Object>) isFiniteF).apply(num)) {
                return ((java.util.function.Function<Object, Object>) just).apply(num);
            }
        } catch (NumberFormatException e) {}
        return nothing;
    };

    public static Object abs = (java.util.function.Function<Object, Object>) (n) -> Math.abs((Double) n);
    public static Object acos = (java.util.function.Function<Object, Object>) (n) -> Math.acos((Double) n);
    public static Object asin = (java.util.function.Function<Object, Object>) (n) -> Math.asin((Double) n);
    public static Object atan = (java.util.function.Function<Object, Object>) (n) -> Math.atan((Double) n);
    public static Object atan2 = (java.util.function.Function<Object, Object>) (y) -> (java.util.function.Function<Object, Object>) (x) -> Math.atan2((Double) y, (Double) x);
    public static Object ceil = (java.util.function.Function<Object, Object>) (n) -> Math.ceil((Double) n);
    public static Object cos = (java.util.function.Function<Object, Object>) (n) -> Math.cos((Double) n);
    public static Object exp = (java.util.function.Function<Object, Object>) (n) -> Math.exp((Double) n);
    public static Object floor = (java.util.function.Function<Object, Object>) (n) -> Math.floor((Double) n);
    public static Object log = (java.util.function.Function<Object, Object>) (n) -> Math.log((Double) n);
    public static Object max = (java.util.function.Function<Object, Object>) (n1) -> (java.util.function.Function<Object, Object>) (n2) -> Math.max((Double) n1, (Double) n2);
    public static Object min = (java.util.function.Function<Object, Object>) (n1) -> (java.util.function.Function<Object, Object>) (n2) -> Math.min((Double) n1, (Double) n2);
    public static Object pow = (java.util.function.Function<Object, Object>) (n) -> (java.util.function.Function<Object, Object>) (p) -> Math.pow((Double) n, (Double) p);
    public static Object remainder = (java.util.function.Function<Object, Object>) (n) -> (java.util.function.Function<Object, Object>) (m) -> (Double) n % (Double) m;
    public static Object round = (java.util.function.Function<Object, Object>) (n) -> Math.rint((Double) n); // Math.round returns long
    public static Object sign = (java.util.function.Function<Object, Object>) (n) -> Math.signum((Double) n);
    public static Object sin = (java.util.function.Function<Object, Object>) (n) -> Math.sin((Double) n);
    public static Object sqrt = (java.util.function.Function<Object, Object>) (n) -> Math.sqrt((Double) n);
    public static Object tan = (java.util.function.Function<Object, Object>) (n) -> Math.tan((Double) n);
    public static Object trunc = (java.util.function.Function<Object, Object>) (x) -> {
        double dx = (Double) x;
        return dx < 0 ? Math.ceil(dx) : Math.floor(dx);
    };
