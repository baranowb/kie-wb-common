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

package org.kie.workbench.common.stunner.client.widgets.presenters.session.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.client.widgets.presenters.AbstractCanvasHandlerViewerTest;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.SessionViewer;
import org.kie.workbench.common.stunner.client.widgets.views.WidgetWrapperView;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvas;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.controls.connection.ConnectionAcceptorControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.containment.ContainmentAcceptorControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.docking.DockingAcceptorControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.select.SelectionControl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.zoom.ZoomControl;
import org.kie.workbench.common.stunner.core.client.command.CanvasCommandManager;
import org.kie.workbench.common.stunner.core.client.session.impl.AbstractClientFullSession;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.mockito.Mock;
import org.uberfire.mvp.ParameterizedCommand;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class SessionPresenterViewTest extends AbstractCanvasHandlerViewerTest {

    @Mock
    private ContextMenuEvent contextMenuEvent;

    @Mock
    private SessionPresenterView tested;

    @Mock
    private NotifySettings settings;

    private ContextMenuHandler handler;

    @Before
    public void setup() throws Exception {
        super.init();

        doAnswer((invocation) -> {
            setFinal(tested,
                     SessionPresenterView.class.getDeclaredField("settings"),
                     settings);
            invocation.callRealMethod();
            return null;
        }).when(tested).init();

        doAnswer((invocation -> {
            invocation.callRealMethod();
            return null;
        }))
                .when(tested).fireEvent(any());

        when(tested.addDomHandler(any(),
                                  any())).thenAnswer((invocation -> {
            handler = invocation.getArgumentAt(0,
                                               ContextMenuHandler.class);
            return null;
        }));

        tested.init();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNoContextMenu() {
        verify(tested).addDomHandler(any(),
                                     any());
        assertNotNull("Handler was null!",
                      handler);
        handler.onContextMenu(contextMenuEvent);
        verify(contextMenuEvent).preventDefault();
        verify(contextMenuEvent).stopPropagation();
    }

    private static void setFinal(Object instance,
                                 Field field,
                                 Object newValue) throws Exception {
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field,
                              field.getModifiers() & ~Modifier.FINAL);
        field.set(instance,
                  newValue);
    }
}
