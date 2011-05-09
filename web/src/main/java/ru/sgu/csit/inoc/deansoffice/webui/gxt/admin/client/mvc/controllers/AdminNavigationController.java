package ru.sgu.csit.inoc.deansoffice.webui.gxt.admin.client.mvc.controllers;

import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.admin.client.mvc.events.AdminAppEvents;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.admin.client.mvc.views.AdminNavigationView;

/**
 * User: Denis Khurtin ( KhurtinDN (a) gmail.com )
 * Date: 4/12/11
 * Time: 9:22 PM
 */
public class AdminNavigationController extends Controller {
    private AdminNavigationView adminNavigationView;

    public AdminNavigationController() {
        registerEventTypes(AdminAppEvents.Init);
    }

    @Override
    protected void initialize() {
        super.initialize();

        adminNavigationView = new AdminNavigationView(this);
    }

    @Override
    public void handleEvent(AppEvent event) {
        forwardToView(adminNavigationView, event);
    }
}