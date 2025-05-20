package com.leng25.sourcerunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        SourceRunner sourceRunner = new SourceRunner(sourceCode);
        String actual = sourceRunner.run("greet");
        assertEquals("✨ Hello from a String-compiled class!", actual);
    }

}
