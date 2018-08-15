/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rapidpm.vaadin.v08.tb.demo

import com.vaadin.ui.Button
import com.vaadin.ui.Composite
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import org.rapidpm.vaadin.addons.framework.ComponentIDGenerator.buttonID

class BasicTestUI : Composite() {

  private val button = Button()
  private val label = Label()

  private var counter = 0

  init {
    label.id = LABEL_ID
    label.value = counter.toString()

    button.id = BUTTON_ID
    button.caption = BUTTON_ID
    button.addClickListener { e -> label.value = (++counter).toString() }

    compositionRoot = VerticalLayout(button, label)
  }

  companion object {
    @JvmField val BUTTON_ID = buttonID().apply(BasicTestUI::class.java, "buttonID")
    @JvmField val LABEL_ID = buttonID().apply(BasicTestUI::class.java, "labelID")
  }


}
