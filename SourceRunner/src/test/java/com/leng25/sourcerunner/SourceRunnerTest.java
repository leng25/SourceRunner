package com.leng25.sourcerunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * SourceRunnerTest
 */
public class SourceRunnerTest {

    @Test
    public void test_base_class() {

        String sourceCode = """
                    package com.example;

                    public class Hello {
                        public String greet() {
                            return "✨ Hello from a String-compiled class!";
                        }
                    }
                """;

        SourceRunner sourceRunner = new SourceRunner(Arrays.asList(sourceCode));
        Object hello = sourceRunner.instanciate("com.example.Hello");
        String actual = sourceRunner.run(hello, "greet");
        assertEquals("✨ Hello from a String-compiled class!", actual);
    }

    @Test
    public void test_multi_classes() {

        String sourceCodeA = """
                    package com.example;

                    public class HelloA {
                        public String greet() {
                            HelloB hellob = new HelloB();
                            return hellob.greet();
                        }
                    }
                """;

        String sourceCodeB = """
                    package com.example;

                    public class HelloB {
                        public String greet() {
                            return "✨ Hello from a String-compiled class!";
                        }
                    }
                """;

        SourceRunner sourceRunner = new SourceRunner(Arrays.asList(sourceCodeA, sourceCodeB));
        Object helloA = sourceRunner.instanciate("com.example.HelloA");
        String actual = sourceRunner.run(helloA, "greet");
        assertEquals("✨ Hello from a String-compiled class!", actual);
    }

    @Test
    public void test_argumetns_class() {

        String sourceCode = """
                    package com.example;

                    public class Hello {
                        public String greet(String greet) {
                            return greet;
                        }
                    }
                """;

        SourceRunner sourceRunner = new SourceRunner(Arrays.asList(sourceCode));
        Object hello = sourceRunner.instanciate("com.example.Hello");
        String actual = sourceRunner.run(hello, "greet", "✨ Hello from a String-compiled class!");
        assertEquals("✨ Hello from a String-compiled class!", actual);
    }

    @Test
    public void test_construcutre_class() {

        String sourceCode = """
                    package com.example;

                    public class Hello {

                        String greet;

                        public Hello(String greet){
                            this.greet = greet;
                        }

                        public String greet() {
                            return greet;
                        }
                    }
                """;

        SourceRunner sourceRunner = new SourceRunner(Arrays.asList(sourceCode));
        Object hello = sourceRunner.instanciate("com.example.Hello", "✨ Hello from a String-compiled class!");
        String actual = sourceRunner.run(hello, "greet");
        assertEquals("✨ Hello from a String-compiled class!", actual);
    }

    @Test
    public void test_record_class() {

        String sourceCode = """
                    package com.example;
                    public record Hello (String name, Integer age) {}
                """;

        SourceRunner sourceRunner = new SourceRunner(Arrays.asList(sourceCode));
        Object hello = sourceRunner.instanciate("com.example.Hello", "Alice", 30);
        String actual = sourceRunner.run(hello, "name");
        assertEquals("Alice", actual);
    }

    @Test
    public void test_ClassA_takes_classB() {

        String sourceCodeA = """
                    package com.example;

                    public class HelloA {
                        public String greet(HelloB hellob) {
                            return hellob.greet();
                        }
                    }
                """;

        String sourceCodeB = """
                    package com.example;

                    public class HelloB {
                        public String greet() {
                            return "✨ Hello from a String-compiled class!";
                        }
                    }
                """;

        SourceRunner sourceRunner = new SourceRunner(Arrays.asList(sourceCodeA, sourceCodeB));
        Object helloA = sourceRunner.instanciate("com.example.HelloA");
        Object helloB = sourceRunner.instanciate("com.example.HelloB");

        String actual = sourceRunner.run(helloA, "greet", helloB);
        assertEquals("✨ Hello from a String-compiled class!", actual);
    }

    @Test
    public void test_ClassA_receives_initialized_ClassB() {

        String sourceCodeA = """
                    package com.example;

                    public class HelloA {
                        public String greet(HelloB b) {
                            return "A says: " + b.getMessage();
                        }
                    }
                """;

        String sourceCodeB = """
                    package com.example;

                    public class HelloB {
                        private final String message;

                        public HelloB(String message) {
                            this.message = message;
                        }

                        public String getMessage() {
                            return this.message;
                        }
                    }
                """;

        SourceRunner sourceRunner = new SourceRunner(Arrays.asList(sourceCodeA, sourceCodeB));
        Object helloA = sourceRunner.instanciate("com.example.HelloA");
        Object helloB = sourceRunner.instanciate("com.example.HelloB",  "👋 from test");

        String actual = sourceRunner.run(helloA, "greet", helloB);
        assertEquals("A says: 👋 from test", actual);
    }

}
