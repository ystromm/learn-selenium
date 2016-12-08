package com.github.ystromm.learn_selenium.webapp.fluentlenium;

import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.github.ystromm.learn_selenium.webapp.PropertyNames;
import com.github.ystromm.learn_selenium.webapp.WebappMain;
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.hook.wait.Wait;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Wait
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class, BackendMain.class}, webEnvironment = RANDOM_PORT)
public class TodosTest extends FluentTest {
    @Rule
    public TestName testName = new TestName();
    @LocalServerPort
    private int localServerPort;

    @Before
    public void setUpProperties() {
        System.setProperty(PropertyNames.BACKEND_SERVER_PORT, Integer.toString(localServerPort));
        System.setProperty(PropertyNames.TODOS_URL, "http://localhost:" + localServerPort);
    }
    
    @Test
    public void title_should_be_todos() {
        goTo("http://localhost:" + localServerPort);
        assertThat(window().title(), equalTo("Todos"));
    }
}
