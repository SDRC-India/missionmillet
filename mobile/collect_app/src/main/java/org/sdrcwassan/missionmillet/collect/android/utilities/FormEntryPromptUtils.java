/*
 * Copyright 2017 Nafundi
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

package org.sdrcwassan.missionmillet.collect.android.utilities;


import android.util.Log;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.data.DateData;
import org.javarosa.core.model.data.DateTimeData;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryPrompt;
import org.sdrcwassan.missionmillet.collect.android.activities.FormHierarchyActivity;
import org.sdrcwassan.missionmillet.collect.android.models.ExternalChoiceData;
import java.util.Date;


public class FormEntryPromptUtils{

    public static String getAnswerText(FormEntryPrompt fep) {
        IAnswerData data = fep.getAnswerValue();
        String text;
        if (data instanceof DateTimeData) {
            text = DateTimeUtils.getDateTimeBasedOnUserLocale((Date) data.getValue(),
                    fep.getQuestion().getAppearanceAttr(), true);
        } else if (data instanceof DateData) {
            text = DateTimeUtils.getDateTimeBasedOnUserLocale((Date) data.getValue(),
                    fep.getQuestion().getAppearanceAttr(), false);
        } else {
           text = fep.getAnswerText();
         /*   String result = getMatchedValue(fep.getAnswerText());
            Log.v("",""+fep.getIndex());

            text = result.length() > 0 ? result : fep.getAnswerText();*/
        }

        return text;
    }
    /**
     * this method is for matching the id and get the corroesponding  items.
     * @author Subhadarshani
     *
     */
    public static String getMatchedValue(String data){
        String result="";
        if(FormHierarchyActivity.externalChoiceList.size()>0){
            for(int i=0;i<FormHierarchyActivity.externalChoiceList.size();i++){
                ExternalChoiceData externalChoiceData = FormHierarchyActivity.externalChoiceList.get(i);
                if(data!=null && externalChoiceData.getId()!=null){
                    if(data.equalsIgnoreCase(externalChoiceData.getId())){
                        result = externalChoiceData.getLabel();
                        break;
                    }
                }
            }
        }

          return result;


    }



}
