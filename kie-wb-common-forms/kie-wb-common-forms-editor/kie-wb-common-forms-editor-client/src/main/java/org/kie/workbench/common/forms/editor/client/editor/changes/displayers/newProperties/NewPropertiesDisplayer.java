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

package org.kie.workbench.common.forms.editor.client.editor.changes.displayers.newProperties;

import java.util.Collection;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.kie.workbench.common.forms.model.FieldDefinition;
import org.uberfire.commons.validation.PortablePreconditions;

@Dependent
public class NewPropertiesDisplayer implements IsElement,
                                               NewPropertiesDisplayerView.Presenter {

    private NewPropertiesDisplayerView view;

    @Inject
    public NewPropertiesDisplayer(NewPropertiesDisplayerView view) {
        this.view = view;
        view.init(this);
    }

    public void clear() {
        view.clear();
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }

    public void showAvailableFields(Collection<FieldDefinition> fieldDefinitions) {
        PortablePreconditions.checkNotNull("fieldDefinitions",
                                           fieldDefinitions);

        fieldDefinitions.forEach(this::showAvailableField);
    }

    protected void showAvailableField(FieldDefinition fieldDefinition) {
        view.showProperty(fieldDefinition.getBinding() + " (" + fieldDefinition.getFieldType().getTypeName() + ")");
    }
}
