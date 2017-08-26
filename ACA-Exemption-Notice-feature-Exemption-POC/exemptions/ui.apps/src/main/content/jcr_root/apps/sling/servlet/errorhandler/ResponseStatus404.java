/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package apps.sling.servlet.errorhandler;

import com.adobe.cq.sightly.WCMUsePojo;

public class ResponseStatus404 extends WCMUsePojo {
    
    @Override
    public void activate() throws Exception {
        getResponse().setStatus(404);
        
        /*
         * Can be configured to fetch /content page using: 
         * 
         * getResponse().getWriter().write("404");
         * getResponse().sendRedirect("/content");
         */
    }
}