package com.indev.scrapx.data.model.type;

import com.indev.scrapx.data.model.ElementSelector;

public class StringElementSelector extends ElementSelector {

    public StringElementSelector(String selector) {
        super(selector,String.class);
    }
}
