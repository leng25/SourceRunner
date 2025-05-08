package com.leng25.sourcerunner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        String fullyQualifiedClassName = "com.example.Hello";

        String sourceCode = """
                    package com.example;

                    public class Hello {
                        public void greet() {
                            System.out.println("Hello from dynamically compiled code!");
                        }
                    }
                """;

        Class<?> clazz;
        try {
            clazz = InMemoryCompiler.compile(fullyQualifiedClassName, sourceCode);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            clazz.getMethod("greet").invoke(instance);
        } catch (Exception e) {
            System.out.println("exeption " + e);
        }
    }
}
