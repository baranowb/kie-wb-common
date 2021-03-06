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

package org.kie.workbench.common.stunner.core.validation.impl;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.kie.workbench.common.stunner.core.validation.ModelBeanViolation;
import org.kie.workbench.common.stunner.core.validation.ModelValidator;

/**
 * An abstraction of the model validator.
 * The ModelValidator (based on jsr303) is not available on server side yet.
 */
public abstract class AbstractModelBeanValidator implements ModelValidator {

    private static Logger LOGGER = Logger.getLogger(AbstractModelBeanValidator.class.getName());

    protected abstract Validator getBeanValidator();

    @Override
    public void validate(final Object entity,
                         final Consumer<Collection<ModelBeanViolation>> callback) {
        LOGGER.log(Level.INFO,
                   "Performing model bean validation.");
        callback.accept(getBeanValidator()
                                .validate(entity)
                                .stream()
                                .map(this::buildViolation)
                                .collect(Collectors.toSet()));
        LOGGER.log(Level.INFO,
                   "Model bean validation completed.");
    }

    private ModelBeanViolation buildViolation(final ConstraintViolation<?> rootViolation) {
        LOGGER.log(Level.INFO,
                   "Bean constraint violation found with message [" + rootViolation.getMessage() + "]");
        return ModelBeanViolationImpl.Builder.build(rootViolation);
    }
}
