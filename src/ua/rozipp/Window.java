package ua.rozipp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class Window {

	protected Shell shell;
	public Text list;
	public Text date_text;
	public Text beginN_text;
	public Text endN_text;
	public Text session_text;
	public Text sessionSK_text;
	public Text sessionN_text;
	public Text files_text;
	public Text warning_text;
	public Text files2_text;
	public Text find_text;
	private ProgressBar progressBar;

	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		Main.my.window = this;
		try{
			Main.my.writeToWindowProperties();
		} catch (Exception e) {}
		try {
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {}
	}

	protected void createContents() {
		shell = new Shell();
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				try {
					Main.webModul.getDriver().close();
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		shell.setSize(821, 559);
		shell.setText("SWT Application");
		shell.setLayout(new BorderLayout(0, 0));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.WEST);
		composite.setLayout(new GridLayout(1, false));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					btnNewButton.setEnabled(false);
					Main.webModul = new WebModul();
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setText("???????????????????????? ???? ??????????");

		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Main.my.readFromWindowProperties();
			}
		});
		btnNewButton_2.setText("???????????????? ????????????????????????");

		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (Main.webModul == null) Main.webModul = new WebModul();
					Main.my.readFromWindowProperties();
					Main.my.checkCreateGolosuvannaWarning();
					int count = Main.my.rishennyaNames.length;
					progressBar.setMinimum(0);
					progressBar.setMaximum(count);
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					warning_text.setText("?????????????? " + count + " ????????????????????");
					for (Integer i = 0; i < count; i++) {
						String number = (i + Main.my.firstNumber) + Main.my.sessionN;
						String doc_name = "???????????????? ?????????????????????? ???? ?????????????? ???" + number;
						Main.webModul.writeElement(Main.my.rishennyaNames[i], number, Main.my.date, Main.my.sessionSK, Main.my.session, doc_name, Main.my.files[i].getAbsolutePath());
						progressBar.setSelection(i);
						shell.redraw();
					}
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setText("???????????? ??????????????????????");

		Button btnNewButton_5 = new Button(composite, SWT.NONE);
		btnNewButton_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Main.my.readFromWindowProperties();
					Main.my.checkCreateRishennaWarning();
					int count = Main.my.files2.length;
					progressBar.setMinimum(0);
					progressBar.setMaximum(count);
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					warning_text.setText("?????????????? " + count + " ????????????");
					for (int i = 0; i < count; i++) {
						String doc_file = Main.my.files2[i].getAbsolutePath();
						String number = i + Main.my.firstNumber + Main.my.sessionN;
						Main.webModul.openFindRishennya(number);
						Main.webModul.editRishennya(number, doc_file);
						progressBar.setSelection(i);
					}
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_5.setText("???????????? ??????????????");

		Button btnNewButton_6 = new Button(composite, SWT.NONE);
		btnNewButton_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Main.my.readFromWindowProperties();
					Main.my.checkCreateRishennaWarning();
					Main.my.checkCreateGolosuvannaWarning();
					int count = Main.my.rishennyaNames.length;
					if (count != Main.my.files2.length) throw new MyException("???? ?????????????????? ???????????? ?????????????????? ???????????????????? ???? ?????????????????? ????????????");

					progressBar.setMinimum(0);
					progressBar.setMaximum(count);
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					warning_text.setText("?????????????? " + count + " ???????????????????? ???? ????????????");
					for (int i = 0; i < count; i++) {
						String number = (i + Main.my.firstNumber) + Main.my.sessionN;
						String doc_name = "???????????????? ?????????????????????? ???? ?????????????? ???" + number;
						Main.webModul.writeElementAndRishennya(Main.my.rishennyaNames[i], number, Main.my.date, Main.my.sessionSK, Main.my.session, doc_name, Main.my.files[i].getAbsolutePath(), Main.my.files2[i].getAbsolutePath());

						progressBar.setSelection(i);
					}
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_6.setText("???????????? ?????????????????????? ???? ??????????????");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		find_text = new Text(composite, SWT.BORDER);
		find_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Main.webModul.openFindRishennya(find_text.getText());
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		button.setText("???????????????? ?????????????? ???? ??????????????");

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String title = Main.webModul.deleteFindRishennya(find_text.getText());
					warning_text.setText("???????????????? ???????????????? \"" + title + "\"");
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		button_1.setText("???????????????? ?????????????? ???? ??????????????");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Button button_2 = new Button(composite, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					list.setText("");
					Integer i = 0;
					while (true) {
						String nomerRishennya = (i + Main.my.firstNumber) + Main.my.sessionN;
						String res = Main.webModul.openFindRishennya(nomerRishennya);
						if (res == null) throw new MyException("???? ?????????????? ?????????????? ?????? ?????????????? " + nomerRishennya);

						String ret = nomerRishennya + "\t" + res;
						System.out.println(ret);
						list.setText(list.getText().concat(ret).concat("\n"));
						i++;
					}
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		button_2.setText("???????????? ?????????????????? ???? ?????? ??????????????");

		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.CENTER);
		composite_1.setLayout(new FillLayout(SWT.VERTICAL));

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));

		Label date_label = new Label(composite_2, SWT.NONE);
		GridData gd_date_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_date_label.widthHint = 142;
		date_label.setLayoutData(gd_date_label);
		date_label.setSize(659, 80);
		date_label.setText("???????? ??????????????????");

		date_text = new Text(composite_2, SWT.BORDER);
		GridData gd_date_text = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_date_text.widthHint = 362;
		date_text.setLayoutData(gd_date_text);
		date_text.setSize(659, 78);

		Label session_label = new Label(composite_2, SWT.NONE);
		GridData gd_session_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_session_label.widthHint = 156;
		session_label.setLayoutData(gd_session_label);
		session_label.setText("?????????? ??????????????");

		session_text = new Text(composite_2, SWT.BORDER);
		session_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label sessionSK_label = new Label(composite_2, SWT.NONE);
		GridData gd_sessionSK_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_sessionSK_label.widthHint = 156;
		sessionSK_label.setLayoutData(gd_session_label);
		sessionSK_label.setText("?????????????????? ??????????????");

		sessionSK_text = new Text(composite_2, SWT.BORDER);
		sessionSK_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label sessionN_label = new Label(composite_2, SWT.NONE);
		sessionN_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		sessionN_label.setText("?????????????????? ?????????? ???? ????????????");

		sessionN_text = new Text(composite_2, SWT.BORDER);
		sessionN_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label beginN_label = new Label(composite_2, SWT.NONE);
		beginN_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		beginN_label.setText("???????????????????? ??????????");

		beginN_text = new Text(composite_2, SWT.BORDER);
		beginN_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label endN_label = new Label(composite_2, SWT.NONE);
		endN_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		endN_label.setText("???????????????? ??????????");

		endN_text = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY);
		endN_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton_3 = new Button(composite_2, SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dlg = new DirectoryDialog(shell, SWT.OPEN);
				String fdir = dlg.open();
				if (fdir != null) Main.my.setFolder(fdir);
			}
		});
		btnNewButton_3.setText("?????????????? ?????????? ?? ????????????????????????");

		files_text = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY);
		files_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton_4 = new Button(composite_2, SWT.NONE);
		btnNewButton_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					DirectoryDialog dlg = new DirectoryDialog(shell, SWT.OPEN);
					String fdir = dlg.open();
					if (fdir != null) Main.my.setFolder2(fdir);
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					Main.window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_4.setText("?????????????? ?????????? ?? ??????????????????");

		files2_text = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY);
		files2_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);

		warning_text = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY);
		warning_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		progressBar = new ProgressBar(composite_2, SWT.NONE);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		list = new Text(composite_1, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);

	}
}
