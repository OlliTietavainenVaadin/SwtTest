package com.vaadin.demo;

import java.awt.Frame;

import javax.swing.JApplet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class SwtDemo {
    private Shell shell;
    private Browser browser = new Browser();
    
    public static void main(String[] args) {
        Display display = new Display();
        SwtDemo application = new SwtDemo();
        Shell shell = application.open(display);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    public Shell open(Display display) {
        shell = new Shell(display);
        shell.setLayout(new FillLayout());
        createMenuBar();
        shell.setText("JxBrowser demo");

        Composite composite = new Composite(shell, SWT.EMBEDDED | SWT.NO_BACKGROUND);
        composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);
        Frame frame = SWT_AWT.new_Frame(composite);
        BrowserView bv = new BrowserView(browser);
        JApplet applet = new JApplet();
        applet.add(bv);
        frame.add(applet);
    
        shell.setSize(800, 600);
        shell.open();
        return shell;
    }

    private Menu createMenuBar() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);
        createFileMenu(menuBar);
        return menuBar;
    }

    private void createFileMenu(Menu menuBar) {
        MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
        item.setText("File");
        Menu menu = new Menu(shell, SWT.DROP_DOWN);
        item.setMenu(menu);
        MenuItem subItem = new MenuItem(menu, SWT.NONE);
        subItem.setText("Open browser");
        subItem.setAccelerator(SWT.MOD1 + 'N');
        subItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                browser.loadURL("http://www.google.com");
            }
        });

        subItem = new MenuItem(menu, SWT.NONE);
        subItem.setText("User agent");
        subItem.setAccelerator(SWT.MOD1 + 'U');
        subItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                browser.loadURL("about:blank");
                JSValue userAgentJS = browser.executeJavaScriptAndReturnValue("window.navigator.userAgent;");
                String userAgent = userAgentJS.getString();
                System.out.println("userAgent:" + userAgent);
            }
        });

        new MenuItem(menu, SWT.SEPARATOR);

        subItem = new MenuItem(menu, SWT.NONE);
        subItem.setText("Exit");
        subItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
    }
}
