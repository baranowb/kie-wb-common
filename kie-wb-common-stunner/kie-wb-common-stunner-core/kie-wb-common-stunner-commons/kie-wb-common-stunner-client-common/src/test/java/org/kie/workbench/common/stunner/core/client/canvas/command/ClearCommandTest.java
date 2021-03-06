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
package org.kie.workbench.common.stunner.core.client.canvas.command;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.core.graph.command.impl.ClearGraphCommand;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ClearCommandTest extends AbstractCanvasCommandTest {

    private ClearCommand tested;

    @Before
    public void setup() throws Exception {
        super.setup();
        this.tested = new ClearCommand();
    }

    @Test
    public void testGetGraphCommand() {
        final ClearGraphCommand graphCommand = (ClearGraphCommand) tested.newGraphCommand(canvasHandler);
        assertNotNull(graphCommand);
        assertEquals(CANVAS_ROOT_UUID,
                     graphCommand.getRootUUID());
    }

    @Test
    public void testGetCanvasCommand() {
        final ClearCanvasCommand canvasCommand = (ClearCanvasCommand) tested.newCanvasCommand(canvasHandler);
        assertNotNull(canvasCommand);
    }
}
