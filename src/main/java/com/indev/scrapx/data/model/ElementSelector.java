package com.indev.scrapx.data.model;

public abstract class ElementSelector {
     private Object selector;
     private Class fieldType;

     public ElementSelector(Object selector,Class type)
     {
         this.selector = selector;
         this.fieldType = type;
     }

    public Object getSelector()
     {
         return this.selector;
     }
     public Class getFieldType()
     {
         return this.fieldType;
     }
}
