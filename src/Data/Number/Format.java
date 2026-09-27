    // Port of Data/Number/Format.js: the ECMAScript methods toPrecision,
    // toFixed, toExponential and Number::toString, which PureScript exposes
    // through Data.Number.Format.

    // Shortest decimal that round-trips, closest to the exact value, ties to
    // even (the ECMAScript digit selection).
    private static java.math.BigDecimal __shortest(double value) {
        java.math.BigDecimal exact = new java.math.BigDecimal(value);
        for (int precision = 1; precision <= 17; precision++) {
            java.math.BigDecimal best = null;
            for (java.math.RoundingMode mode : new java.math.RoundingMode[]{
                java.math.RoundingMode.HALF_EVEN, java.math.RoundingMode.HALF_UP,
                java.math.RoundingMode.FLOOR, java.math.RoundingMode.CEILING}) {
                java.math.BigDecimal candidate = exact.round(new java.math.MathContext(precision, mode));
                if (Double.parseDouble(candidate.toString()) != value) continue;
                if (best == null) { best = candidate; continue; }
                int cmp = candidate.subtract(exact).abs().compareTo(best.subtract(exact).abs());
                if (cmp < 0
                    || (cmp == 0
                        && candidate.stripTrailingZeros().unscaledValue().testBit(0) == false
                        && best.stripTrailingZeros().unscaledValue().testBit(0))) {
                    best = candidate;
                }
            }
            if (best != null) return best;
        }
        return exact.round(new java.math.MathContext(17, java.math.RoundingMode.HALF_EVEN));
    }

    // Number::toString: plain notation for exponents in [-6, 20], exponential
    // otherwise, shortest digits, e+/- without zero padding.
    private static String __ecmaNumber(double value) {
        if (Double.isNaN(value)) return "NaN";
        if (Double.isInfinite(value)) return value > 0 ? "Infinity" : "-Infinity";
        if (value == 0.0) return "0";
        boolean negative = value < 0;
        java.math.BigDecimal decimal = __shortest(value).abs().stripTrailingZeros();
        double magnitude = Math.abs(value);
        String body;
        if (magnitude >= 1e-6 && magnitude < 1e21) {
            body = decimal.toPlainString();
        } else {
            int digits = decimal.precision();
            int exponent = digits - decimal.scale() - 1;
            String mantissa = decimal.unscaledValue().toString();
            body = (digits == 1 ? mantissa : mantissa.charAt(0) + "." + mantissa.substring(1))
                + "e" + (exponent >= 0 ? "+" : "") + exponent;
        }
        return negative ? "-" + body : body;
    }

    private static String __exponential(int digits, double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return __ecmaNumber(value);
        if (value == 0.0) {
            String zeros = digits == 0 ? "" : "." + "0".repeat(digits);
            return "0" + zeros + "e+0";
        }
        boolean negative = value < 0;
        java.math.BigDecimal rounded = new java.math.BigDecimal(value).abs()
            .round(new java.math.MathContext(digits + 1, java.math.RoundingMode.HALF_UP))
            .stripTrailingZeros();
        int exponent = rounded.precision() - rounded.scale() - 1;
        String mantissa = rounded.unscaledValue().toString();
        StringBuilder builder = new StringBuilder();
        if (negative) builder.append('-');
        builder.append(mantissa.charAt(0));
        if (digits > 0) {
            builder.append('.');
            for (int index = 1; index <= digits; index++) {
                builder.append(index < mantissa.length() ? mantissa.charAt(index) : '0');
            }
        }
        builder.append('e').append(exponent >= 0 ? "+" : "").append(exponent);
        return builder.toString();
    }

    private static String __fixed(int digits, double value) {
        if (Math.abs(value) >= 1e21) return __ecmaNumber(value);
        return new java.math.BigDecimal(value).setScale(digits, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private static String __precision(int digits, double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return __ecmaNumber(value);
        if (value == 0.0) return digits <= 1 ? "0" : "0." + "0".repeat(digits - 1);
        boolean negative = value < 0;
        java.math.BigDecimal rounded = new java.math.BigDecimal(value).abs()
            .round(new java.math.MathContext(digits, java.math.RoundingMode.HALF_UP))
            .stripTrailingZeros();
        int exponent = rounded.precision() - rounded.scale() - 1;
        String digitsText = rounded.unscaledValue().toString();
        String body;
        if (exponent < -6 || exponent >= digits) {
            StringBuilder builder = new StringBuilder();
            builder.append(digitsText.charAt(0));
            if (digits > 1) {
                builder.append('.');
                for (int index = 1; index < digits; index++) {
                    builder.append(index < digitsText.length() ? digitsText.charAt(index) : '0');
                }
            }
            builder.append('e').append(exponent >= 0 ? "+" : "").append(exponent);
            body = builder.toString();
        } else if (exponent >= 0) {
            StringBuilder builder = new StringBuilder();
            int point = exponent + 1;
            for (int index = 0; index < digits; index++) {
                if (index == point) builder.append('.');
                builder.append(index < digitsText.length() ? digitsText.charAt(index) : '0');
            }
            body = builder.toString();
        } else {
            body = "0." + "0".repeat(-exponent - 1) + digitsText;
        }
        return negative ? "-" + body : body;
    }

    public static Object toPrecisionNative = (java.util.function.Function<Object, Object>) (d) ->
        (java.util.function.Function<Object, Object>) (num) ->
            __precision(((Number) d).intValue(), ((Number) num).doubleValue());

    public static Object toFixedNative = (java.util.function.Function<Object, Object>) (d) ->
        (java.util.function.Function<Object, Object>) (num) ->
            __fixed(((Number) d).intValue(), ((Number) num).doubleValue());

    public static Object toExponentialNative = (java.util.function.Function<Object, Object>) (d) ->
        (java.util.function.Function<Object, Object>) (num) ->
            __exponential(((Number) d).intValue(), ((Number) num).doubleValue());

    public static Object toString = (java.util.function.Function<Object, Object>) (num) ->
        __ecmaNumber(((Number) num).doubleValue());
