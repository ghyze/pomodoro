package nl.ghyze.pomodoro;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.font.TextAttribute;
import java.awt.im.InputMethodHighlight;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.peer.*;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class TestToolkit extends Toolkit {
    @Override
    protected DesktopPeer createDesktopPeer(Desktop target) throws HeadlessException {
        return null;
    }

    @Override
    protected ButtonPeer createButton(Button target) throws HeadlessException {
        return null;
    }

    @Override
    protected TextFieldPeer createTextField(TextField target) throws HeadlessException {
        return null;
    }

    @Override
    protected LabelPeer createLabel(Label target) throws HeadlessException {
        return null;
    }

    @Override
    protected ListPeer createList(List target) throws HeadlessException {
        return null;
    }

    @Override
    protected CheckboxPeer createCheckbox(Checkbox target) throws HeadlessException {
        return null;
    }

    @Override
    protected ScrollbarPeer createScrollbar(Scrollbar target) throws HeadlessException {
        return null;
    }

    @Override
    protected ScrollPanePeer createScrollPane(ScrollPane target) throws HeadlessException {
        return null;
    }

    @Override
    protected TextAreaPeer createTextArea(TextArea target) throws HeadlessException {
        return null;
    }

    @Override
    protected ChoicePeer createChoice(Choice target) throws HeadlessException {
        return null;
    }

    @Override
    protected FramePeer createFrame(Frame target) throws HeadlessException {
        return null;
    }

    @Override
    protected CanvasPeer createCanvas(Canvas target) {
        return null;
    }

    @Override
    protected PanelPeer createPanel(Panel target) {
        return null;
    }

    @Override
    protected WindowPeer createWindow(Window target) throws HeadlessException {
        return null;
    }

    @Override
    protected DialogPeer createDialog(Dialog target) throws HeadlessException {
        return null;
    }

    @Override
    protected MenuBarPeer createMenuBar(MenuBar target) throws HeadlessException {
        return null;
    }

    @Override
    protected MenuPeer createMenu(Menu target) throws HeadlessException {
        return null;
    }

    @Override
    protected PopupMenuPeer createPopupMenu(PopupMenu target) throws HeadlessException {
        return null;
    }

    @Override
    protected MenuItemPeer createMenuItem(MenuItem target) throws HeadlessException {
        return null;
    }

    @Override
    protected FileDialogPeer createFileDialog(FileDialog target) throws HeadlessException {
        return null;
    }

    @Override
    protected CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem target) throws HeadlessException {
        return null;
    }

    @Override
    protected FontPeer getFontPeer(String name, int style) {
        return null;
    }

    @Override
    public Dimension getScreenSize() throws HeadlessException {
        return null;
    }

    @Override
    public int getScreenResolution() throws HeadlessException {
        return 0;
    }

    @Override
    public ColorModel getColorModel() throws HeadlessException {
        return null;
    }

    @Override
    public String[] getFontList() {
        return new String[0];
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        return null;
    }

    @Override
    public void sync() {

    }

    @Override
    public Image getImage(String filename) {
        return null;
    }

    @Override
    public Image getImage(URL url) {
        return null;
    }

    @Override
    public Image createImage(String filename) {
        return null;
    }

    @Override
    public Image createImage(URL url) {
        return null;
    }

    @Override
    public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
        return false;
    }

    @Override
    public int checkImage(Image image, int width, int height, ImageObserver observer) {
        return 0;
    }

    @Override
    public Image createImage(ImageProducer producer) {
        return null;
    }

    @Override
    public Image createImage(byte[] imagedata, int imageoffset, int imagelength) {
        return null;
    }

    @Override
    public PrintJob getPrintJob(Frame frame, String jobtitle, Properties props) {
        return null;
    }

    @Override
    public void beep() {

    }

    @Override
    public Clipboard getSystemClipboard() throws HeadlessException {
        return null;
    }

    @Override
    protected EventQueue getSystemEventQueueImpl() {
        return null;
    }

    @Override
    public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dge) throws InvalidDnDOperationException {
        return null;
    }

    @Override
    public boolean isModalityTypeSupported(Dialog.ModalityType modalityType) {
        return false;
    }

    @Override
    public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType modalExclusionType) {
        return false;
    }

    @Override
    public Map<TextAttribute, ?> mapInputMethodHighlight(InputMethodHighlight highlight) throws HeadlessException {
        return null;
    }

    @Override
    public Insets getScreenInsets(GraphicsConfiguration gc){
        return new Insets(0,0,0,0);
    }
}
