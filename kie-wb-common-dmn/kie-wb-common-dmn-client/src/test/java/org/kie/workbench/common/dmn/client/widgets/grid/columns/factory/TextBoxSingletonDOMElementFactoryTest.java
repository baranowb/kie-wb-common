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

package org.kie.workbench.common.dmn.client.widgets.grid.columns.factory;

import com.ait.lienzo.test.LienzoMockitoTestRunner;
import org.junit.runner.RunWith;
import org.kie.workbench.common.dmn.client.widgets.grid.columns.factory.dom.TextBoxDOMElement;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.grid.GridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer;

import static org.mockito.Mockito.*;

@RunWith(LienzoMockitoTestRunner.class)
public class TextBoxSingletonDOMElementFactoryTest extends BaseSingletonDOMElementFactoryTest<TextBoxSingletonDOMElementFactory, TextBoxDOMElement> {

    @Override
    protected TextBoxSingletonDOMElementFactory getFactoryForAttachDomElementTest() {
        return new TextBoxSingletonDOMElementFactory(gridPanel,
                                                     gridLayer,
                                                     gridWidget,
                                                     sessionManager,
                                                     sessionCommandManager,
                                                     uiModelMapper) {
            @Override
            public TextBoxDOMElement createDomElement(final GridLayer gridLayer,
                                                      final GridWidget gridWidget,
                                                      final GridBodyCellRenderContext context) {
                return spy(super.createDomElement(gridLayer,
                                                  gridWidget,
                                                  context));
            }
        };
    }

    @Override
    protected TextBoxSingletonDOMElementFactory getFactoryForFlushTest() {
        return new TextBoxSingletonDOMElementFactory(gridPanel,
                                                     gridLayer,
                                                     gridWidget,
                                                     sessionManager,
                                                     sessionCommandManager,
                                                     uiModelMapper);
    }
}
