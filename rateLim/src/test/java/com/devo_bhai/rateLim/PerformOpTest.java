package com.devo_bhai.rateLim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PerformOpTest {
    @Mock
    public PerformOp performOp;

    @Test
    public void testAdd() {
        doReturn(10).when(performOp).add(2, 5);
        int res = performOp.add(2, 5);
        Assertions.assertEquals(10, res);
        verify(performOp, times(1)).add(2, 5);
    }
}
