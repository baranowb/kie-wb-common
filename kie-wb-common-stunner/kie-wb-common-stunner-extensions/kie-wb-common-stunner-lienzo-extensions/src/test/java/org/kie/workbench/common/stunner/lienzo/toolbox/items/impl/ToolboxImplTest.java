/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.lienzo.toolbox.items.impl;

import java.util.Iterator;
import java.util.function.BiConsumer;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.BoundingPoints;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.Direction;
import com.ait.lienzo.test.LienzoMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.lienzo.toolbox.grid.AutoGrid;
import org.kie.workbench.common.stunner.lienzo.toolbox.grid.Point2DGrid;
import org.kie.workbench.common.stunner.lienzo.toolbox.items.DecoratedItem;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.uberfire.mvp.Command;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(LienzoMockitoTestRunner.class)
public class ToolboxImplTest {

    private final BoundingBox shapeBoundingBox = new BoundingBox(0d,
                                                                 0d,
                                                                 33d,
                                                                 33d);

    private final BoundingBox boundingBox = new BoundingBox(0d,
                                                            0d,
                                                            100d,
                                                            200d);
    @Mock
    private BiConsumer<Group, Command> showExecutor;

    @Mock
    private BiConsumer<Group, Command> hideExecutor;

    @Mock
    private ItemGridImpl wrapped;

    @Mock
    private Group group;

    @Mock
    private IPrimitive primitive;

    @Mock
    private BoundingPoints boundingPoints;

    private ToolboxImpl tested;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        when(wrapped.getBoundingBox()).thenReturn(() -> boundingBox);
        when(wrapped.onRefresh(any(Command.class))).thenReturn(wrapped);
        when(wrapped.asPrimitive()).thenReturn(group);
        when(wrapped.getPrimitive()).thenReturn(primitive);
        when(group.getAlpha()).thenReturn(0d);
        when(group.getComputedBoundingPoints()).thenReturn(boundingPoints);
        when(group.getBoundingBox()).thenReturn(boundingBox);
        when(boundingPoints.getBoundingBox()).thenReturn(boundingBox);
        doAnswer(invocationOnMock -> {
            ((Command) invocationOnMock.getArguments()[0]).execute();
            ((Command) invocationOnMock.getArguments()[1]).execute();
            return wrapped;
        }).when(wrapped).show(any(Command.class),
                              any(Command.class));
        doAnswer(invocationOnMock -> {
            ((Command) invocationOnMock.getArguments()[0]).execute();
            ((Command) invocationOnMock.getArguments()[1]).execute();
            return wrapped;
        }).when(wrapped).hide(any(Command.class),
                              any(Command.class));
        tested = new ToolboxImpl(() -> shapeBoundingBox,
                                 wrapped)
                .useHideExecutor(hideExecutor)
                .useShowExecutor(showExecutor);
    }

    @Test
    public void testInit() {
        assertEquals(primitive,
                     tested.getPrimitive());
        assertEquals(group,
                     tested.asPrimitive());
        assertEquals(boundingBox,
                     tested.getBoundingBox().get());
        assertFalse(tested.isVisible());
        verify(wrapped,
               times(1)).useShowExecutor(eq(showExecutor));
        verify(wrapped,
               times(1)).useHideExecutor(eq(hideExecutor));
    }

    @Test
    public void testAt() {
        ToolboxImpl cascade = tested.at(Direction.EAST);
        assertEquals(tested,
                     cascade);
        assertEquals(Direction.EAST,
                     tested.getAt());
    }

    @Test
    public void testOffset() {
        Point2D o = new Point2D(50,
                                25);
        ToolboxImpl cascade = tested.offset(o);
        assertEquals(tested,
                     cascade);
        assertEquals(o,
                     tested.getOffset());
    }

    @Test
    public void testGrid() {
        Point2DGrid grid = mock(Point2DGrid.class);
        when(wrapped.getGrid()).thenReturn(grid);
        ToolboxImpl cascade = tested.grid(grid);
        assertEquals(tested,
                     cascade);
        verify(wrapped,
               times(1)).grid(eq(grid));
    }

    @Test
    public void testUpdateGridSize() {
        AutoGrid grid = spy(new AutoGrid.Builder()
                                    .forBoundingBox(boundingBox)
                                    .build());
        when(wrapped.getGrid()).thenReturn(grid);
        ToolboxImpl cascade = tested.grid(grid);
        assertEquals(tested,
                     cascade);
        verify(wrapped,
               times(1)).grid(eq(grid));
        verify(grid,
               times(1)).setSize(eq(53d),
                                 eq(53d));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIterate() {
        Iterator<DecoratedItem> iterator = mock(Iterator.class);
        when(wrapped.iterator()).thenReturn(iterator);
        assertEquals(iterator,
                     tested.iterator());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddItem() {
        DecoratedItem item = mock(DecoratedItem.class);
        tested.add(item);
        verify(wrapped,
               times(1)).add(eq(item));
    }

    @Test
    public void testShow() {
        final Command before = mock(Command.class);
        final Command after = mock(Command.class);
        tested.show(before,
                    after);
        verify(wrapped,
               times(1)).show(any(Command.class),
                              any(Command.class));
        verify(wrapped,
               never()).hide(any(Command.class),
                             any(Command.class));
        verify(before,
               times(1)).execute();
        verify(after,
               times(1)).execute();
        ArgumentCaptor<Point2D> pc = ArgumentCaptor.forClass(Point2D.class);
        verify(group,
               times(1)).setLocation(pc.capture());
        Point2D point = pc.getValue();
        assertEquals(33d,
                     point.getX(),
                     0);
        assertEquals(0d,
                     point.getY(),
                     0);
    }

    @Test
    public void testHide() {
        final Command before = mock(Command.class);
        final Command after = mock(Command.class);
        tested.hide(before,
                    after);
        verify(wrapped,
               times(1)).hide(eq(before),
                              any(Command.class));
        verify(wrapped,
               never()).show(any(Command.class),
                             any(Command.class));
        verify(before,
               times(1)).execute();
        verify(after,
               times(1)).execute();
    }

    @Test
    public void testDestroy() {
        tested.destroy();
        verify(wrapped,
               times(1)).destroy();
    }
}
