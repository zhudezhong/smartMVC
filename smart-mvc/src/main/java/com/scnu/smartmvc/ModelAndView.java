package com.scnu.smartmvc;

import com.scnu.smartmvc.http.HttpStatus;
import org.springframework.ui.Model;

public class ModelAndView {
    private Object view;
    private Model model;
    private HttpStatus status;

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
