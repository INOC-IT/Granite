package ru.sgu.csit.inoc.deansoffice.webui.gxt.admin.client.components.info;

import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.admin.client.components.grids.StaffGrid;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.admin.client.mvc.events.AdminEvents;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.admin.client.services.StaffService;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.common.shared.model.EmployeeModel;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.common.shared.mvc.events.CommonEvents;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.common.shared.utils.BaseAsyncCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Denis Khurtin ( KhurtinDN (a) gmail.com )
 * Date: 4/15/11
 * Time: 3:11 PM
 */
public class StaffPanel extends ContentPanel {
    private StaffGrid staffGrid = new StaffGrid();

    public StaffPanel() {
        setHeading("Сотрудники университета");
        setLayout(new FitLayout());

        final RowEditor<EmployeeModel> rowEditor = new RowEditor<EmployeeModel>();
        rowEditor.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
        rowEditor.addListener(Events.AfterEdit, new Listener<RowEditorEvent>() {
            @Override
            public void handleEvent(RowEditorEvent rowEditorEvent) {
                mask("Сохраниние измененного сотрудника");

                final EmployeeModel employeeModel = (EmployeeModel) rowEditorEvent.getRecord().getModel();

                StaffService.Util.getInstance().update(employeeModel, new BaseAsyncCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        staffGrid.getStore().commitChanges();
                        unmask();
                        Dispatcher.forwardEvent(CommonEvents.Info, "Сотрудник успешно изменен!");
                        Dispatcher.forwardEvent(AdminEvents.StaffChanged);
                    }
                });
            }
        });

        staffGrid.addPlugin(rowEditor);

        final Button addStaffButton = new Button("Добавить", IconHelper.createStyle("addButton-icon"));
        addStaffButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                rowEditor.stopEditing(false);
                mask("Добавление нового сотрудника");

                StaffService.Util.getInstance().create(new BaseAsyncCallback<EmployeeModel>() {
                    @Override
                    public void onSuccess(EmployeeModel employeeModel) {
                        staffGrid.getStore().add(employeeModel);
                        rowEditor.startEditing(staffGrid.getStore().indexOf(employeeModel), true);
                        unmask();

                        Dispatcher.forwardEvent(CommonEvents.Info, "Сотрудник успешно добавлен!");
                        Dispatcher.forwardEvent(AdminEvents.StaffAdded);
                    }
                });
            }
        });

        final Button editStaffButton = new Button("Редактировать", IconHelper.createStyle("editButton-icon"));
        editStaffButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {

                final EmployeeModel employeeModel = staffGrid.getSelectionModel().getSelectedItem();

                if (employeeModel == null) {
                    Dispatcher.forwardEvent(CommonEvents.InfoWithConfirmation, "Выберите, пожалуйста, сотрудника!");
                } else {
                    rowEditor.startEditing(staffGrid.getStore().indexOf(employeeModel), true);
                }
            }
        });

        final Button removeStaffButton = new Button("Удалить", IconHelper.createStyle("removeButton-icon"));
        removeStaffButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                final List<EmployeeModel> staffList = staffGrid.getSelectionModel().getSelectedItems();

                if (staffList.isEmpty()) {
                    Dispatcher.forwardEvent(CommonEvents.InfoWithConfirmation, "Выберите, пожалуйста, сотрудников!");
                } else {
                    final List<Long> employeeIdList = new ArrayList<Long>(staffList.size());

                    for (EmployeeModel employeeModel : staffList) {
                        employeeIdList.add(employeeModel.getId());
                    }

                    MessageBox.confirm("Удаление сотрудников",
                            "Вы действително хотите удалить выбранных сотрудников?",
                            new Listener<MessageBoxEvent>() {
                                @Override
                                public void handleEvent(MessageBoxEvent be) {
                                    if (be.getDialog().yesText.equals(be.getButtonClicked().getText())) {
                                        rowEditor.stopEditing(false);
                                        mask("Удаление выбранных сотрудников");

                                        StaffService.Util.getInstance().delete(employeeIdList,
                                                new BaseAsyncCallback<Void>() {
                                                    @Override
                                                    public void onSuccess(Void result) {
                                                        for (EmployeeModel employeeModel : staffList) {
                                                            staffGrid.getStore().remove(employeeModel);
                                                        }

                                                        unmask();
                                                        Dispatcher.forwardEvent(CommonEvents.Info, "Сотрудники успешно удалены!");
                                                        Dispatcher.forwardEvent(AdminEvents.StaffDeleted);
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });

        final ToolBar staffGridToolBar = new ToolBar();
        staffGridToolBar.add(addStaffButton);
        staffGridToolBar.add(new SeparatorToolItem());
        staffGridToolBar.add(editStaffButton);
        staffGridToolBar.add(new SeparatorToolItem());
        staffGridToolBar.add(removeStaffButton);

        setTopComponent(staffGridToolBar);

        add(staffGrid);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        staffGrid.reload();
    }
}
