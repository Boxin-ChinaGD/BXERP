package wpos.presenter;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import wpos.utils.Shared;

@ContextConfiguration(locations = {"classpath:ApplicationContext.xml"})
public class BaseTestNGSpringContextTest extends AbstractTestNGSpringContextTests {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();

    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }
}
