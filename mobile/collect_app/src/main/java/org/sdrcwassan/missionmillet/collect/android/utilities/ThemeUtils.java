/*
 * Copyright (C) 2018 Shobhit Agarwal
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.sdrcwassan.missionmillet.collect.android.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleRes;
import android.util.TypedValue;

import org.sdrcwassan.missionmillet.collect.android.R;
import org.sdrcwassan.missionmillet.collect.android.preferences.GeneralSharedPreferences;
import org.sdrcwassan.missionmillet.collect.android.preferences.PreferenceKeys;


public final class ThemeUtils {

    private final Context context;

    public ThemeUtils(Context context) {
        this.context = context;
    }

    @StyleRes
    public int getAppTheme() {
//        return isDarkTheme() ? R.style.DarkAppTheme : R.style.LightAppTheme;
        return 0;
    }

    @StyleRes
    public int getSettingsTheme() {
//        return isDarkTheme() ? R.style.AppTheme_SettingsTheme_Dark : R.style.AppTheme_SettingsTheme_Light;
        return 0;
    }

    @StyleRes
    public int getBottomDialogTheme() {
//        return isDarkTheme() ? R.style.DarkMaterialDialogSheet : R.style.LightMaterialDialogSheet;
        return 0;
    }

    public boolean isHoloDialogTheme(int theme) {
        return theme == android.R.style.Theme_Holo_Light_Dialog ||
                theme == android.R.style.Theme_Holo_Dialog;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @StyleRes
    public int getMaterialDialogTheme() {
        return isDarkTheme() ?
                android.R.style.Theme_Material_Dialog :
                android.R.style.Theme_Material_Light_Dialog;
    }

    @StyleRes
    public int getHoloDialogTheme() {
        return isDarkTheme() ?
                android.R.style.Theme_Holo_Dialog :
                android.R.style.Theme_Holo_Light_Dialog;
    }

    private int getAttributeValue(@AttrRes int resId) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(resId, outValue, true);
        return outValue.data;
    }

    public int getAccountPickerTheme() {
        return isDarkTheme() ? 0 : 1;
    }

    public boolean isDarkTheme() {
//        String theme = (String) GeneralSharedPreferences.getInstance().get(PreferenceKeys.KEY_APP_THEME);
//        return theme.equals(context.getString(R.string.app_theme_dark));
        return false;
    }

    /**
     * @return Text color for the current {@link android.content.res.Resources.Theme}
     */
    @ColorInt
    public int getPrimaryTextColor() {
//        return getAttributeValue(R.attr.primaryTextColor);
        return 0;
    }

    /**
     * @return Accent color for the current {@link android.content.res.Resources.Theme}
     */
    @ColorInt
    public int getAccentColor() {
        return getAttributeValue(R.attr.colorAccent);
    }

    /**
     * @return Icon color for the current {@link android.content.res.Resources.Theme}
     */
    @ColorInt
    public int getIconColor() {
//        return getAttributeValue(R.attr.iconColor);
        return 0;
    }
}
