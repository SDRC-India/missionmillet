package org.sdrcwassan.missionmillet.collect.android.activities;


import org.sdrcwassan.missionmillet.collect.android.provider.FormsProviderAPI;

import static org.sdrcwassan.missionmillet.collect.android.utilities.ApplicationConstants.SortingOrder.BY_DATE_ASC;
import static org.sdrcwassan.missionmillet.collect.android.utilities.ApplicationConstants.SortingOrder.BY_DATE_DESC;
import static org.sdrcwassan.missionmillet.collect.android.utilities.ApplicationConstants.SortingOrder.BY_NAME_ASC;
import static org.sdrcwassan.missionmillet.collect.android.utilities.ApplicationConstants.SortingOrder.BY_NAME_DESC;

abstract class FormListActivity extends AppListActivity {
    protected String getSortingOrder() {
        String sortingOrder = FormsProviderAPI.FormsColumns.DISPLAY_NAME + " ASC";
        switch (getSelectedSortingOrder()) {
            case BY_NAME_ASC:
                sortingOrder = FormsProviderAPI.FormsColumns.DISPLAY_NAME + " ASC";
                break;
            case BY_NAME_DESC:
                sortingOrder = FormsProviderAPI.FormsColumns.DISPLAY_NAME + " DESC";
                break;
            case BY_DATE_ASC:
                sortingOrder = FormsProviderAPI.FormsColumns.DATE + " ASC";
                break;
            case BY_DATE_DESC:
                sortingOrder = FormsProviderAPI.FormsColumns.DATE + " DESC";
                break;
        }
        return sortingOrder;
    }
}