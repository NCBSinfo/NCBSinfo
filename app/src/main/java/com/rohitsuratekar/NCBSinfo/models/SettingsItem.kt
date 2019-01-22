package com.rohitsuratekar.NCBSinfo.models

import com.rohitsuratekar.NCBSinfo.common.SettingsActions.VIEW_HEADER


class SettingsItem {
    var icon: Int = 0
    var title: String? = null
    var viewType: Int = 0
    var description: String? = null
    var action: Int = 0
    var isDisabled: Boolean = false

    constructor(type: Int) {

        viewType = type
    }
    constructor(title: String) {
        this.title = title
        viewType = VIEW_HEADER
    }
}
