package com.vaadin.demo;

import java.awt.Frame;

import javax.swing.JApplet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class SwtDemo {
    private Shell shell;
    private Browser browser = new Browser();
    private org.eclipse.swt.browser.Browser swtBrowser;
    
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
        swtBrowser  = new org.eclipse.swt.browser.Browser(shell, SWT.NONE);
        swtBrowser.addProgressListener(new ProgressListener() {
            int i = 0;
            
            @Override
            public void completed(ProgressEvent event) {
                System.out.println("SWT browser completed: " + event.toString());
                System.out.println("I is now: " + i);
                
            }
            
            @Override
            public void changed(ProgressEvent event) {
                System.out.println("SWT browser changed: " + event.toString());
                i++;
            }
        });
        
        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onStartLoadingFrame(StartLoadingEvent event) {
                if (event.isMainFrame()) {
                    System.out.println("Main frame has started loading");
                }
            }
            
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                    System.out.println("Main frame has finished loading");
                }
            }
        });
        
                
        Composite composite = new Composite(shell, SWT.EMBEDDED | SWT.NO_BACKGROUND);

        Frame frame = SWT_AWT.new_Frame(composite);
        BrowserView bv = new BrowserView(browser);
        JApplet applet = new JApplet();
        
        applet.add(bv);
        frame.add(applet);
    
        shell.setSize(1200, 800);
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
                browser.loadURL("https://www.google.com/?query=JxBrowser");
                swtBrowser.setUrl("https://www.google.fi/?query=SWTBrowser");
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
