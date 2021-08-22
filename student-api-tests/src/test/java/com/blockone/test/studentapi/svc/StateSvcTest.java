package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.utils.WorkspaceUtils;
import com.cfg.SpringConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class StateSvcTest {

    @Spy
    @InjectMocks
    private StateSvc stateSvc;

    @Mock
    private WorkspaceUtils workspaceUtils;

    @Mock
    private StatePropertiesSvc statePropertiesSvc;

    @Test
    public void testExpandExpression_withMultipleVars() {
        final String expected = "Test_abc_def_xyz";
        final String expression = "Test_${var1}_${var2}_${var3}";
        doReturn("abc").when(stateSvc).getStringVar("var1");
        doReturn("def").when(stateSvc).getStringVar("var2");
        doReturn("xyz").when(stateSvc).getStringVar("var3");
        Assert.assertEquals(expected, stateSvc.expandExpression(expression));
    }

    @Test
    public void testExpandExpression_withSingleVarInString() {
        final String expected = "Test_abc_def";
        final String expression = "Test_${var1}_def";
        doReturn("abc").when(stateSvc).getStringVar("var1");
        Assert.assertEquals(expected, stateSvc.expandExpression(expression));
    }


    @Test
    public void testExpandExpression_withNoVarInExpression() {
        final String expected = "Test";
        final String expression = "Test";
        Assert.assertEquals(expected, stateSvc.expandExpression(expression));
    }

    @Test
    public void testExpandExpression_withNull() {
        final String expression = null;
        Assert.assertNull(stateSvc.expandExpression(expression));
    }
}
