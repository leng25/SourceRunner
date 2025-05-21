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
        String actual = sourceRunner.run("com.example.Hello","greet");
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
        String actual = sourceRunner.run("com.example.HelloA","greet");
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
        String actual = sourceRunner.run("com.example.Hello","greet","✨ Hello from a String-compiled class!");
        assertEquals("✨ Hello from a String-compiled class!", actual);
    }


}
