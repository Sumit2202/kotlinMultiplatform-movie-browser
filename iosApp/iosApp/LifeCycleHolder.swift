//
//  LifeCycleHolder.swift
//  iosApp
//
//  Created by Ananya Sharma on 2023-08-15.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared

class LifeCycleHolder : ObservableObject {

    let lifecycle:LifecycleRegistry

    init(){
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()

        lifecycle.onCreate()
    }

    deinit {
        lifecycle.onDestroy()
    }

}
