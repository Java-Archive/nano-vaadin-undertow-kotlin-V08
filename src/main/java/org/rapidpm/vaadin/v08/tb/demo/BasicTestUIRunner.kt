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

import com.vaadin.annotations.PreserveOnRefresh
import com.vaadin.annotations.Push
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.UI
import org.apache.meecrowave.Meecrowave

import javax.servlet.annotation.WebServlet

object BasicTestUIRunner {

  @JvmStatic
  fun main(args: Array<String>) {
    Meecrowave(object : Meecrowave.Builder() {
      init {
        //        randomHttpPort();
        httpPort = 8080
        isTomcatScanning = true
        isTomcatAutoSetup = false
        isHttp2 = true
      }
    })
        .bake()
        .await()
  }

  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI::class)
  class MyProjectServlet : VaadinServlet()

  @PreserveOnRefresh
  @Push
  class MyUI : UI() {
    override fun init(request: VaadinRequest) {
      content = BasicTestUI()
    }
  }
}
