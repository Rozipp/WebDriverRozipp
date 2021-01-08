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

	static MyProperties my;
	static Window window;
	static WebModul webModul;

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

	public static void main(String[] args) {
		try {
			my = new MyProperties();
			window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		my.window = this;
		try{
			my.writeToWindowProperties();
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
					webModul.getDriver().close();
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
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
					webModul = new WebModul();
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setText("Підключитися до сайту");

		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				my.readFromWindowProperties();
			}
		});
		btnNewButton_2.setText("Зберегти налаштування");

		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (webModul == null) webModul = new WebModul();
					my.readFromWindowProperties();
					my.checkCreateGolosuvannaWarning();
					int count = my.rishennyaNames.length;
					progressBar.setMinimum(0);
					progressBar.setMaximum(count);
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					warning_text.setText("Доданою " + count + " голосувань");
					for (Integer i = 0; i < count; i++) {
						String number = (i + my.firstNumber) + my.sessionN;
						String doc_name = "Поіменне голосування по рішенню №" + number;
						webModul.writeElement(my.rishennyaNames[i], number, my.date, my.sessionSK, my.session, doc_name, my.files[i].getAbsolutePath());
						progressBar.setSelection(i);
						shell.redraw();
					}
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setText("Додати голосування");

		Button btnNewButton_5 = new Button(composite, SWT.NONE);
		btnNewButton_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					my.readFromWindowProperties();
					my.checkCreateRishennaWarning();
					int count = my.files2.length;
					progressBar.setMinimum(0);
					progressBar.setMaximum(count);
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					warning_text.setText("Доданою " + count + " рішень");
					for (int i = 0; i < count; i++) {
						String doc_file = my.files2[i].getAbsolutePath();
						String number = i + my.firstNumber + my.sessionN;
						webModul.openFindRishennya(number);
						webModul.editRishennya(number, doc_file);
						progressBar.setSelection(i);
					}
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_5.setText("Додати рішення");

		Button btnNewButton_6 = new Button(composite, SWT.NONE);
		btnNewButton_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					my.readFromWindowProperties();
					my.checkCreateRishennaWarning();
					my.checkCreateGolosuvannaWarning();
					int count = my.rishennyaNames.length;
					if (count != my.files2.length) throw new MyException("Не однаковий розмір кількості голосувань та кількості рішень");

					progressBar.setMinimum(0);
					progressBar.setMaximum(count);
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					warning_text.setText("Доданою " + count + " голосувань та рішень");
					for (int i = 0; i < count; i++) {
						String number = (i + my.firstNumber) + my.sessionN;
						String doc_name = "Поіменне голосування по рішенню №" + number;
						webModul.writeElementAndRishennya(my.rishennyaNames[i], number, my.date, my.sessionSK, my.session, doc_name, my.files[i].getAbsolutePath(), my.files2[i].getAbsolutePath());

						progressBar.setSelection(i);
					}
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_6.setText("Додати голосування та рішення");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		find_text = new Text(composite, SWT.BORDER);
		find_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					webModul.openFindRishennya(find_text.getText());
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		button.setText("Відкрити рішення за номером");

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String title = webModul.deleteFindRishennya(find_text.getText());
					warning_text.setText("Виделено документ \"" + title + "\"");
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		button_1.setText("Видалити рішення за номером");
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
						String nomerRishennya = (i + my.firstNumber) + my.sessionN;
						String res = webModul.openFindRishennya(nomerRishennya);
						if (res == null) throw new MyException("Не знайшов рішення під номером " + nomerRishennya);

						String ret = nomerRishennya + "\t" + res;
						System.out.println(ret);
						list.setText(list.getText().concat(ret).concat("\n"));
						i++;
					}
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		button_2.setText("Знайти посилання на всі рішення");

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
		date_label.setText("Дата прийняття");

		date_text = new Text(composite_2, SWT.BORDER);
		GridData gd_date_text = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_date_text.widthHint = 362;
		date_text.setLayoutData(gd_date_text);
		date_text.setSize(659, 78);

		Label session_label = new Label(composite_2, SWT.NONE);
		GridData gd_session_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_session_label.widthHint = 156;
		session_label.setLayoutData(gd_session_label);
		session_label.setText("Сесія буквами");

		session_text = new Text(composite_2, SWT.BORDER);
		session_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label sessionSK_label = new Label(composite_2, SWT.NONE);
		GridData gd_sessionSK_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_sessionSK_label.widthHint = 156;
		sessionSK_label.setLayoutData(gd_session_label);
		sessionSK_label.setText("Скликання буквами");

		sessionSK_text = new Text(composite_2, SWT.BORDER);
		sessionSK_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label sessionN_label = new Label(composite_2, SWT.NONE);
		sessionN_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		sessionN_label.setText("Приставка сесії до номера");

		sessionN_text = new Text(composite_2, SWT.BORDER);
		sessionN_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label beginN_label = new Label(composite_2, SWT.NONE);
		beginN_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		beginN_label.setText("Початковий номер");

		beginN_text = new Text(composite_2, SWT.BORDER);
		beginN_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label endN_label = new Label(composite_2, SWT.NONE);
		endN_label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		endN_label.setText("Кінцевий номер");

		endN_text = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY);
		endN_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton_3 = new Button(composite_2, SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dlg = new DirectoryDialog(shell, SWT.OPEN);
				String fdir = dlg.open();
				if (fdir != null) my.setFolder(fdir);
			}
		});
		btnNewButton_3.setText("Вибрати папку з голосуванням");

		files_text = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY);
		files_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton_4 = new Button(composite_2, SWT.NONE);
		btnNewButton_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					DirectoryDialog dlg = new DirectoryDialog(shell, SWT.OPEN);
					String fdir = dlg.open();
					if (fdir != null) my.setFolder2(fdir);
				} catch (Exception e1) {
					warning_text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					window.warning_text.setText(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_4.setText("Вибрати папку з рішеннями");

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
