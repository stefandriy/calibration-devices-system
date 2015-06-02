package com.softserve.edu.service.storage.impl;

public class SaveOptions {
    private boolean override;
    
    public SaveOptions(){
        
    }
    public SaveOptions(boolean overide) {
        this.override = overide;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    
    
}
