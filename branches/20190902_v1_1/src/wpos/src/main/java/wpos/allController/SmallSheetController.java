package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import com.sun.jndi.toolkit.url.Uri;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.*;
import wpos.allEnum.StageType;
import wpos.allEnum.ThreadMode;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.ConfigGeneralHttpEvent;
import wpos.event.SmallSheetHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.ConfigGeneralSQLiteEvent;
import wpos.event.UI.SmallSheetSQLiteEvent;
import wpos.listener.PlatFormHandlerMessage;
import wpos.listener.Subscribe;
import wpos.listener.radioButtonSelectLinstener;
import wpos.model.*;
import wpos.presenter.BasePresenter;
import wpos.presenter.ConfigGeneralPresenter;
import wpos.presenter.SmallSheetFramePresenter;
import wpos.presenter.SmallSheetTextPresenter;
import wpos.utils.*;
import javafx.scene.paint.Color;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import static wpos.allController.AllFragmentViewController.currentSmallSheetID;

@Component("smallSheetController")
public class SmallSheetController extends BaseController implements radioButtonSelectLinstener, PlatFormHandlerMessage {
    public static final long Default_SmallSheetID_INPos = 1l; // ?????????????????????????????????ID???????????????????????????????????????????????????????????????????????????????????????????????????
    public static final int Default_SmallSheetListSlveSize = 20; // ??????????????????????????????size??????  ??????????????????????????????????????????size??????????????????20

    private static Logger log = Logger.getLogger(SmallSheetController.class);

    private final String defaultText = "";
    private static final String IMAGE_FILE_LOCATION = "temp.jpeg";
    final int CUT_BIG_OK = 1 << 4;
    private final int CUT_BIG_REQUEST = 1 << 6;

    AllFragmentViewController viewController;

    public void setAllFragmentViewController(AllFragmentViewController c) {
        viewController = c;
        initEventAndBO();
        onCreate();
    }

    public SmallSheetController() {
    }

    private File bmpFrameLogo;

    /**
     * ???????????????????????????????????????????????????????????????
     */
    private final static int MAX_Length_EditText = 30;
    /**
     * ????????????????????????????????????
     */
    private final static int GRAVITY_LEFT = 51;
    /**
     * ????????????????????????????????????
     */
    private final static int GRAVITY_CENTRE = 17;
    /**
     * ????????????????????????????????????
     */
    private final static int GRAVITY_RIGHT = 53;


    //??????????????????????????????
    private int cursorPos;
    //???????????????EditText????????????
    private String inputAfterText;
    //???????????????EditText?????????
    private boolean resetText;

    //??????????????????????????????
    private int designTicketBottom_cursorPos;
    //???????????????EditText????????????
    private String designTicketBottom_inputAfterText;
    //???????????????EditText?????????
    private boolean designTicketBottom_resetText;

    //??????????????????????????????
    private int designFooter1_cursorPos;
    //???????????????EditText????????????
    private String designFooter1_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter1_resetText;

    //??????????????????????????????
    private int designFooter2_cursorPos;
    //???????????????EditText????????????
    private String designFooter2_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter2_resetText;

    //??????????????????????????????
    private int designFooter3_cursorPos;
    //???????????????EditText????????????
    private String designFooter3_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter3_resetText;

    //??????????????????????????????
    private int designFooter4_cursorPos;
    //???????????????EditText????????????
    private String designFooter4_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter4_resetText;

    //??????????????????????????????
    private int designFooter5_cursorPos;
    //???????????????EditText????????????
    private String designFooter5_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter5_resetText;

    //??????????????????????????????
    private int designFooter6_cursorPos;
    //???????????????EditText????????????
    private String designFooter6_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter6_resetText;

    //??????????????????????????????
    private int designFooter7_cursorPos;
    //???????????????EditText????????????
    private String designFooter7_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter7_resetText;

    //??????????????????????????????
    private int designFooter8_cursorPos;
    //???????????????EditText????????????
    private String designFooter8_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter8_resetText;

    //??????????????????????????????
    private int designFooter9_cursorPos;
    //???????????????EditText????????????
    private String designFooter9_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter9_resetText;

    //??????????????????????????????
    private int designFooter10_cursorPos;
    //???????????????EditText????????????
    private String designFooter10_inputAfterText;
    //???????????????EditText?????????
    private boolean designFooter10_resetText;
    //????????????????????????????????????position ,????????????????????????????????????????????????????????????????????????????????????????????????????????????
    private int SelectPosition;

    ImageView selected_no_logo;
    ImageView selected_from_album;

    final FileChooser fileChooser = new FileChooser();

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????
     */
    private final static int ROW_NO = 20;
    private final int[] left_shift_blank_number = new int[ROW_NO];
    private final int[] right_shift_blank_number = new int[ROW_NO];

    //    private ArrayAdapter<String> spinner_Adapter;//????????????????????????
    private final List<SmallSheetText> smallSheetTextLists = new ArrayList<>();
    private List<Integer> smallSheetFrame_IDList = new ArrayList<Integer>();//???????????????????????????????????????FrameID

    private Uri selectImg;
    private Integer selectedFrameID;//???????????????????????????ID

    @Resource
    public SmallSheetFramePresenter smallSheetFramePresenter;
    @Resource
    public SmallSheetTextPresenter smallSheetTextPresenter;
    @Resource
    public SmallSheetSQLiteBO smallSheetSQLiteBO;
    @Resource
    public SmallSheetHttpBO smallSheetHttpBO;
    @Resource
    public SmallSheetSQLiteEvent smallSheetSQLiteEvent;
    @Resource
    public SmallSheetHttpEvent smallSheetHttpEvent;
    @Resource
    public ConfigGeneralPresenter configGeneralPresenter;
    @Resource
    public ConfigGeneralSQLiteBO configGeneralSQLiteBO;
    @Resource
    public ConfigGeneralHttpBO configGeneralHttpBO;
    @Resource
    public ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;
    @Resource
    public ConfigGeneralHttpEvent configGeneralHttpEvent;

    /**
     * ???????????????????????????????????????????????????????????????????????????100???????????????????????????.xml???????????????MaxLength????????????????????????
     */
    private static int MAX_LENGTH_Content = 100;


    public void onCreate() {
        // ?????? currentSmallSheetID ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        currentSmallSheetID = BaseSQLiteBO.INVALID_INT_ID;
        initEventAndBO();
        initDesignHeaderFooterEtc();
        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            currentSmallSheetID = viewController.retrieveCurrentSmallSheetIDFromConfig();
        }
        if (currentSmallSheetID != BaseSQLiteBO.INVALID_INT_ID) {
            initSmallSheetData(currentSmallSheetID);
        } else {
            ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
        }


    }

    /**
     * ?????????Header???Footer???
     */
    private void initDesignHeaderFooterEtc() {
        viewController.designDelimiterToRepeat.textProperty().addListener((observable, oldValue, newValue) -> {
            String str = viewController.designDelimiterToRepeat.getText();
            try {
                // ??????if??????????????????????????????????????????????????????????????????????????????????????????getBytes("utf-8").length = 1???getBytes("gbk").length == 1???????????????getBytes("utf-8").length = 3???getBytes("gbk").length == 2?????????getBytes("utf-8").length = 3???getBytes("gbk").length == 1
                if (viewController.designDelimiterToRepeat.getText().getBytes("utf-8").length == 3 && viewController.designDelimiterToRepeat.getText().getBytes("gbk").length == 1) {
                    viewController.designDelimiterToRepeat.setText("");
                    ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                } else {
                    for (int j = 1; j <= 8; j++) {
                        str = str.concat(str);
                    }
                    viewController.delimiterDiy1.setText(str);
                    viewController.delimiterDiy2.setText(str);
                    viewController.delimiterDiy3.setText(str);
                    viewController.delimiterDiy4.setText(str);
                    viewController.delimiterDiy5.setText(str);
                    viewController.delimiterDiy6.setText(str);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        viewController.designHeader.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!resetText) {
                cursorPos = viewController.designHeader.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                inputAfterText = newValue.toString();
            }

            if (!resetText) {
                int count = (newValue.length() - oldValue.length());
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(cursorPos, cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designHeader.setText(inputAfterText);
                            CharSequence text = viewController.designHeader.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            viewController.templateHeader.setVisible(true);
                            viewController.templateHeader.setManaged(true);
                            viewController.delimiterDiy2.setVisible(true);
                            viewController.delimiterDiy2.setManaged(true);
                            if (!"".equals(viewController.designHeader.getText().toString())) {
                                viewController.templateHeader.setText(viewController.designHeader.getText().toString());
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        // ?????????????????????count?????????0(count?????????????????????)???????????????????????????charSequence?????????
                        // charSequence.subSequence(cursorPos, cursorPos + count)???????????????????????????????????????????????????????????????
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        viewController.designHeader.setText(inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    viewController.templateHeader.setVisible(true);
                    viewController.templateHeader.setManaged(true);
                    viewController.delimiterDiy2.setVisible(true);
                    viewController.delimiterDiy2.setManaged(true);
                    if (!"".equals(viewController.designHeader.getText().toString())) {
                        viewController.templateHeader.setText(viewController.designHeader.getText().toString());
                    }
                }
            } else {
                resetText = false;
            }

            if ("".equals(newValue.toString())) {
                viewController.templateHeader.setVisible(false);
                viewController.templateHeader.setManaged(false);
                viewController.delimiterDiy2.setVisible(false);
                viewController.delimiterDiy2.setManaged(false);
                viewController.templateHeader.setText(newValue);
            }
        });

        viewController.designTicketBottom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designTicketBottom_resetText) {
                designTicketBottom_cursorPos = viewController.designTicketBottom.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designTicketBottom_inputAfterText = newValue;
            }

            if (!designTicketBottom_resetText) {
                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designTicketBottom_cursorPos, designTicketBottom_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designTicketBottom_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designTicketBottom.setText(designTicketBottom_inputAfterText);
                            CharSequence text = viewController.designTicketBottom.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designTicketBottom.getText())) {
                                viewController.templateTicketBottom.setText(viewController.designTicketBottom.getText());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        viewController.designTicketBottom.setText(designTicketBottom_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designTicketBottom.getText())) {
                        viewController.templateTicketBottom.setText(viewController.designTicketBottom.getText());
                    }
                }
            } else {
                designTicketBottom_resetText = false;
            }
        });

        viewController.designFooter1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter1_resetText) {
                designFooter1_cursorPos = viewController.designFooter1.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter1_inputAfterText = newValue.toString();
            }

            if (!designFooter1_resetText) {
                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter1_cursorPos, designFooter1_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter1_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter1.setText(designFooter1_inputAfterText);
                            CharSequence text = viewController.designFooter1.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter1.getText().toString())) {
                                viewController.templateFooter1.setText(viewController.designFooter1.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        System.out.println("????????????");
                        //      designFooter1.setText(designFooter1_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter1.getText().toString())) {
                        viewController.templateFooter1.setText(viewController.designFooter1.getText().toString());
                    }
                }
            } else {
                designFooter1_resetText = false;
            }
        });

        viewController.designFooter2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter2_resetText) {
                designFooter2_cursorPos = viewController.designFooter2.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter2_inputAfterText = newValue.toString();

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter2_cursorPos, designFooter2_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter2_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter2.setText(designFooter2_inputAfterText);
                            CharSequence text = viewController.designFooter1.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter2.getText().toString())) {
                                viewController.templateFooter2.setText(viewController.designFooter2.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //  designFooter2.setText(designFooter2_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter2.getText().toString())) {
                        viewController.templateFooter2.setText(viewController.designFooter2.getText().toString());
                    }
                }
            } else {
                designFooter2_resetText = false;
            }
            setFooterTextChange(viewController.designFooter2, viewController.templateFooter2, viewController.designFooter2_layout);
        });

        viewController.designFooter3.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter3_resetText) {
                designFooter3_cursorPos = viewController.designFooter3.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter3_inputAfterText = newValue;

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter3_cursorPos, designFooter3_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter3_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter3.setText(designFooter3_inputAfterText);
                            CharSequence text = viewController.designFooter1.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter3.getText().toString())) {
                                viewController.templateFooter3.setText(viewController.designFooter3.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //  designFooter3.setText(designFooter3_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter3.getText().toString())) {
                        viewController.templateFooter3.setText(viewController.designFooter3.getText().toString());
                    }
                }
            } else {
                designFooter3_resetText = false;
            }
            setFooterTextChange(viewController.designFooter3, viewController.templateFooter3, viewController.designFooter3Layout);
        });

        viewController.designFooter4.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter4_resetText) {
                if (!designFooter4_resetText) {
                    designFooter4_cursorPos = viewController.designFooter4.getCaretPosition();
                    // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                    // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                    // inputAfterText????????????????????????????????????????????????
                    designFooter4_inputAfterText = newValue.toString();
                }

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter4_cursorPos, designFooter4_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter4_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter4.setText(designFooter4_inputAfterText);
                            CharSequence text = viewController.designFooter4.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter4.getText().toString())) {
                                viewController.templateFooter4.setText(viewController.designFooter4.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //   designFooter4.setText(designFooter4_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter4.getText().toString())) {
                        viewController.templateFooter4.setText(viewController.designFooter4.getText().toString());
                    }
                }
            } else {
                designFooter4_resetText = false;
            }
            setFooterTextChange(viewController.designFooter4, viewController.templateFooter4, viewController.designFooter4Layout);
        });

        viewController.designFooter5.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter5_resetText) {
                designFooter5_cursorPos = viewController.designFooter5.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter5_inputAfterText = newValue.toString();

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter5_cursorPos, designFooter5_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter5_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter5.setText(designFooter5_inputAfterText);
                            CharSequence text = viewController.designFooter5.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter5.getText().toString())) {
                                viewController.templateFooter5.setText(viewController.designFooter5.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //   designFooter5.setText(designFooter5_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter5.getText().toString())) {
                        viewController.templateFooter5.setText(viewController.designFooter5.getText().toString());
                    }
                }
            } else {
                designFooter5_resetText = false;
            }
            setFooterTextChange(viewController.designFooter5, viewController.templateFooter5, viewController.designFooter5Layout);
        });

        viewController.designFooter6.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter6_resetText) {
                designFooter6_cursorPos = viewController.designFooter6.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter6_inputAfterText = newValue.toString();

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter6_cursorPos, designFooter6_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter6_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter6.setText(designFooter6_inputAfterText);
                            CharSequence text = viewController.designFooter6.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter6.getText().toString())) {
                                viewController.templateFooter6.setText(viewController.designFooter6.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //      designFooter6.setText(designFooter6_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter6.getText().toString())) {
                        viewController.templateFooter6.setText(viewController.designFooter6.getText().toString());
                    }
                }
            } else {
                designFooter6_resetText = false;
            }
            setFooterTextChange(viewController.designFooter6, viewController.templateFooter6, viewController.designFooter6Layout);
        });

        viewController.designFooter7.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter7_resetText) {
                designFooter7_cursorPos = viewController.designFooter7.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter7_inputAfterText = newValue.toString();

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter7_cursorPos, designFooter7_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter7_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter7.setText(designFooter7_inputAfterText);
                            CharSequence text = viewController.designFooter7.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter7.getText().toString())) {
                                viewController.templateFooter7.setText(viewController.designFooter7.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //     designFooter7.setText(designFooter7_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter7.getText().toString())) {
                        viewController.templateFooter7.setText(viewController.designFooter7.getText().toString());
                    }
                }
            } else {
                designFooter7_resetText = false;
            }
            setFooterTextChange(viewController.designFooter7, viewController.templateFooter7, viewController.designFooter7Layout);
        });

        viewController.designFooter8.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter8_resetText) {
                designFooter8_cursorPos = viewController.designFooter8.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter8_inputAfterText = newValue.toString();

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter8_cursorPos, designFooter8_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter8_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter8.setText(designFooter8_inputAfterText);
                            CharSequence text = viewController.designFooter8.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter8.getText().toString())) {
                                viewController.templateFooter4.setText(viewController.designFooter8.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //    designFooter8.setText(designFooter8_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter8.getText().toString())) {
                        viewController.templateFooter8.setText(viewController.designFooter8.getText().toString());
                    }
                }
            } else {
                designFooter8_resetText = false;
            }
            setFooterTextChange(viewController.designFooter8, viewController.templateFooter8, viewController.designFooter8Layout);
        });

        viewController.designFooter9.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter9_resetText) {
                designFooter9_cursorPos = viewController.designFooter9.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter9_inputAfterText = newValue.toString();

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter9_cursorPos, designFooter9_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter9_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter9.setText(designFooter9_inputAfterText);
                            CharSequence text = viewController.designFooter9.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter9.getText().toString())) {
                                viewController.templateFooter9.setText(viewController.designFooter9.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //    designFooter9.setText(designFooter9_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter9.getText().toString())) {
                        viewController.templateFooter9.setText(viewController.designFooter9.getText().toString());
                    }
                }
            } else {
                designFooter9_resetText = false;
            }
            setFooterTextChange(viewController.designFooter9, viewController.templateFooter9, viewController.designFooter9Layout);
        });

        viewController.designFooter10.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!designFooter10_resetText) {
                designFooter10_cursorPos = viewController.designFooter10.getCaretPosition();
                // ?????????charSequence.toString()???????????????charSequence??????????????????charSequence???
                // ?????????inputAfterText???charSequence??????????????????????????????????????????s????????????
                // inputAfterText????????????????????????????????????????????????
                designFooter10_inputAfterText = newValue.toString();

                int count = newValue.length() - oldValue.length();
                if (count >= 2) {//????????????????????????????????????2
                    try {
                        CharSequence input = newValue.subSequence(designFooter10_cursorPos, designFooter10_cursorPos + count);
                        if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                            designFooter10_resetText = true;
                            ToastUtil.toast("???????????????Emoji????????????", ToastUtil.LENGTH_SHORT);
                            //?????????????????????????????????????????????????????????????????????
                            viewController.designFooter10.setText(designFooter10_inputAfterText);
                            CharSequence text = viewController.designFooter10.getText();
                            // TODO
//                                if (text instanceof Spannable) {
//                                    Spannable spanText = (Spannable) text;
//                                    Selection.setSelection(spanText, text.length());
//                                }
                        } else { // ?????????????????????
                            if (!"".equals(viewController.designFooter10.getText().toString())) {
                                viewController.templateFooter10.setText(viewController.designFooter10.getText().toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        //      designFooter10.setText(designFooter10_inputAfterText);
                        return;
                    }
                } else { // ????????????????????????????????????
                    if (!"".equals(viewController.designFooter10.getText().toString())) {
                        viewController.templateFooter10.setText(viewController.designFooter10.getText().toString());
                    }
                }
            } else {
                designFooter10_resetText = false;
            }
            setFooterTextChange(viewController.designFooter10, viewController.templateFooter10, viewController.designFooter10Layout);
        });
    }

    public void initEventAndBO() {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);
        smallSheetSQLiteEvent.setId(BaseEvent.EVENT_ID_SmallSheetStage);
        smallSheetHttpEvent.setId(BaseEvent.EVENT_ID_SmallSheetStage);
        //
        smallSheetSQLiteBO.setHttpEvent(smallSheetHttpEvent);
        smallSheetSQLiteBO.setSqLiteEvent(smallSheetSQLiteEvent);
        //
        smallSheetHttpBO.setHttpEvent(smallSheetHttpEvent);
        smallSheetHttpBO.setSqLiteEvent(smallSheetSQLiteEvent);
        //
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        //
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);

        configGeneralSQLiteEvent.setId(BaseEvent.EVENT_ID_SmallSheetStage);
        configGeneralHttpEvent.setId(BaseEvent.EVENT_ID_SmallSheetStage);
        //
        configGeneralSQLiteBO.setHttpEvent(configGeneralHttpEvent);
        configGeneralSQLiteBO.setSqLiteEvent(configGeneralSQLiteEvent);
        ///
        configGeneralHttpBO.setHttpEvent(configGeneralHttpEvent);
        configGeneralHttpBO.setSqLiteEvent(configGeneralSQLiteEvent);
        //
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        //
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SmallSheetStage) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                    onDoneApplyServerDataSuccess(event);
                } else {
                    log.error("????????????????????????????????????event=" + event.toString());
                }
            } else if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done) {
                    viewController.closeLoadingDialog();
                    selectedFrameID = event.getBaseModel1().getID();
                    log.info("??????????????????????????????????????????");
                    Message message = new Message();
                    message.what = 1;
                    PlatForm.get().sendMessage(message);
                } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync) {
                    viewController.closeLoadingDialog();
                    Message message = new Message();
                    message.what = 3;
                    message.obj = "????????????????????????????????????????????????";
                    PlatForm.get().sendMessage(message);
                    log.info("??????????????????????????????????????????");
                } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done) {
                    onBeforeDeleteServerDone(event);
                } else {
                    log.error("????????????????????????????????????event=" + event.toString());
                }
            } else {
                log.error("????????????????????????event=" + event.toString());
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    private void onBeforeDeleteServerDone(SmallSheetSQLiteEvent event) {
        viewController.closeLoadingDialog();
        log.info("?????????????????????????????????????????????");
        //
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setSql("where F_Name = '%s'");
        configGeneral.setConditions(new String[]{"ACTIVE_SMALLSHEET_ID"});
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        configGeneral = configGeneralList.get(0); //???0???????????????????????????????????????
        if (configGeneral != null) {
            if (configGeneral.getValue().equals(String.valueOf(event.getBaseModel1().getID()))) {
                configGeneral.setValue(String.valueOf(Default_SmallSheetID_INPos));
                configGeneral.setReturnObject(1); //...
                configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
//                            configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
                if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral)) {
                    log.error("?????????????????????????????????configGeneral=" + configGeneral);
//                                ToastUtil.toast(appApplication, "?????????????????????????????????", ToastUtil.LENGTH_SHORT);
//                                closeLoadingDialog();//????????????UI??????
                }
                currentSmallSheetID = Integer.valueOf(String.valueOf(Default_SmallSheetID_INPos));// ??????????????????????????????????????????configGeneralSQLiteBO.updateAsync()??????????????????
            }
        }
        Message message = new Message();
        message.what = 6;
        PlatForm.get().sendMessage(message);
    }

    private void onDoneApplyServerDataSuccess(SmallSheetSQLiteEvent event) {
        if (BasePresenter.SYNC_Type_U.equals(smallSheetHttpEvent.getSyncType())) {
            closeLoadingDialog();
            viewController.closeLoadingDialog();
            smallSheetHttpEvent.setSyncType("");
            Message message = new Message();
            message.what = 4;
            PlatForm.get().sendMessage(message);
        } else if (BasePresenter.SYNC_Type_C.equals(smallSheetHttpEvent.getSyncType())) {
            closeLoadingDialog();
            viewController.closeLoadingDialog();
            bmpFrameLogo = null;
            smallSheetHttpEvent.setSyncType("");
            selectedFrameID = event.getBaseModel1().getID();
            //????????????????????????????????????
            Message message = new Message();
            message.what = 1;
            PlatForm.get().sendMessage(message);
        } else if (BasePresenter.SYNC_Type_D.equals(smallSheetHttpEvent.getSyncType())) {
            bmpFrameLogo = null;
            smallSheetHttpEvent.setSyncType("");

            ConfigGeneral configGeneral = new ConfigGeneral();
            configGeneral.setSql("where F_Name = '%s'");
            configGeneral.setConditions(new String[]{"ACTIVE_SMALLSHEET_ID"});
            List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
            configGeneral = configGeneralList.get(0);
            if (configGeneral != null) {
                if (configGeneral.getValue().equals(String.valueOf(event.getBaseModel1().getID()))) {
                    configGeneral.setValue(String.valueOf(Default_SmallSheetID_INPos));
                    configGeneral.setReturnObject(1); //...
                    configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
//                            configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
                    if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral)) {
                        log.error("?????????????????????????????????configGeneral=" + configGeneral);
//                                ToastUtil.toast(appApplication, "update ConfigGeneral?????????", ToastUtil.LENGTH_SHORT);
//                                closeLoadingDialog();
                    }
                    currentSmallSheetID = Integer.valueOf(String.valueOf(Default_SmallSheetID_INPos));// ??????????????????????????????????????????configGeneralSQLiteBO.updateAsync()??????????????????

                    return;
                }
            }

            //???????????????????????????????????????
            Message message = new Message();
            message.what = 6;
            PlatForm.get().sendMessage(message);
        } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsyncC_Done) {
//            closeLoadingDialog();
            viewController.closeLoadingDialog();
            //????????????????????????????????????
            Message message = new Message();
            message.what = 5;
            PlatForm.get().sendMessage(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SmallSheetStage) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {

            } else if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField) {
                Message message = new Message();
                message.what = 3;
                message.obj = "?????????????????????????????????????????????????????????????????????";
                PlatForm.get().sendMessage(message);
            } else {
                Message message = new Message();
                message.what = 3;
                message.obj = "???????????????????????????????????????????????????";
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SmallSheetStage) {
            event.onEvent();
            if ("UPDATE_DONE".equals(event.getData())) { // TODO
                viewController.closeLoadingDialog();
                event.setData("");
                if (currentSmallSheetID != BaseSQLiteBO.INVALID_ID) {
                    ToastUtil.toast("????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                }

                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                    ToastUtil.toast("????????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                } else {
                    Message message = new Message();
                    message.what = 2;
                    PlatForm.get().sendMessage(message);
                }
            }
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SmallSheetStage) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            } else {
                viewController.closeLoadingDialog();
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            }
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    public void designTopLogo_click() {
        onClickSelectLogo();
    }

    public void add_footer_click() {
        if (!viewController.designFooter2_layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter2_layout, viewController.designFooter2);
            setAddFooterView();
        } else if (!viewController.designFooter3Layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter3Layout, viewController.designFooter3);
            setAddFooterView();
        } else if (!viewController.designFooter4Layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter4Layout, viewController.designFooter4);
            setAddFooterView();
        } else if (!viewController.designFooter5Layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter5Layout, viewController.designFooter5);
            setAddFooterView();
        } else if (!viewController.designFooter6Layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter6Layout, viewController.designFooter6);
            setAddFooterView();
        } else if (!viewController.designFooter7Layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter7Layout, viewController.designFooter7);
            setAddFooterView();
        } else if (!viewController.designFooter8Layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter8Layout, viewController.designFooter8);
            setAddFooterView();
        } else if (!viewController.designFooter9Layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter9Layout, viewController.designFooter9);
            setAddFooterView();
        } else if (!viewController.designFooter10Layout.isVisible()) {
            setFooterLayoutIsVisible(viewController.designFooter10Layout, viewController.designFooter10);
            setAddFooterView();
        }
    }

    public void designDeleteFooter2_click() {
        delete_footer(viewController.templateFooter2, viewController.designFooter2, viewController.designFooter2_layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void designDeleteFooter3_click() {
        delete_footer(viewController.templateFooter3, viewController.designFooter3, viewController.designFooter3Layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void designDeleteFooter4_click() {
        delete_footer(viewController.templateFooter4, viewController.designFooter4, viewController.designFooter4Layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void designDeleteFooter5_click() {
        delete_footer(viewController.templateFooter5, viewController.designFooter5, viewController.designFooter5Layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void designDeleteFooter6_click() {
        delete_footer(viewController.templateFooter6, viewController.designFooter6, viewController.designFooter6Layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void designDeleteFooter7_click() {
        delete_footer(viewController.templateFooter7, viewController.designFooter7, viewController.designFooter7Layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void designDeleteFooter8_click() {
        delete_footer(viewController.templateFooter8, viewController.designFooter8, viewController.designFooter8Layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void designDeleteFooter9_click() {
        delete_footer(viewController.templateFooter9, viewController.designFooter9, viewController.designFooter9Layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void designDeleteFooter10_click() {
        delete_footer(viewController.templateFooter10, viewController.designFooter10, viewController.designFooter10Layout);
        viewController.add_footer.setVisible(true);
        viewController.add_footer.setManaged(true);
    }

    public void templateHeader_click() {
        setTextBackgroudColor();
        viewController.templateHeader.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateHeader.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateHeader);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateHeader);
        });
        setTextGravity(viewController.templateHeader);
    }

    public void templateDocumentNumber_click() {
        setTextBackgroudColor();

        viewController.templateDocumentNumber.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateDocumentNumber.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateDocumentNumber);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateDocumentNumber);
        });
        setTextGravity(viewController.templateDocumentNumber);
    }

    public void templateDate_click() {
        setTextBackgroudColor();
        viewController.templateDate.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateDate.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateDate);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateDate);
        });
        setTextGravity(viewController.templateDate);
    }

    public void templateGoodsName_click() {
        selectGoodsListLayout(viewController.templateGoodsName, null);
    }

    public void templateGoodsNumber_click() {
        selectGoodsListLayout(viewController.templateGoodsNumber, null);
    }

    public void templateSubtotal_click() {
        selectGoodsListLayout(viewController.templateSubtotal, null);
    }

//    public void templateGoodsNameLayout_click() {
//        selectGoodsListLayout(viewController.templateFirstGoodsName, viewController.templateGoodsNameLayout);
//    }
//
//    public void templateGoodsNumberLayout_click() {
//        selectGoodsListLayout(viewController.templateFirstGoodsNumber, viewController.templateGoodsNumberLayout);
//    }
//
//    public void templateSubtotalLayout_click() {
//        selectGoodsListLayout(viewController.templateFirstSubtotal, viewController.templateSubtotalLayout);
//    }

    public void templateTotalMoney_click() {
        setTextBackgroudColor();
        viewController.templateTotalMoney.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateTotalMoney.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateTotalMoney);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateTotalMoney);
        });
        setTextGravity(viewController.templateTotalMoney);
    }

    public void templatePaymentMethod_click() {
        setTextBackgroudColor();
        viewController.templatePaymentMethod.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templatePaymentMethod.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templatePaymentMethod);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templatePaymentMethod);
        });
        setTextGravity(viewController.templatePaymentMethod);
    }

    public void templateDiscount_click() {
        setTextBackgroudColor();
        viewController.templateDiscount.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateDiscount.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateDiscount);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateDiscount);
        });
        setTextGravity(viewController.templateDiscount);
    }

    public void templatePayable_click() {
        setTextBackgroudColor();
        viewController.templatePayable.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templatePayable.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templatePayable);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templatePayable);
        });
        setTextGravity(viewController.templatePayable);
    }

    public void templatePaymentMethod1_click() {
        setTextBackgroudColor();
        viewController.templatePaymentMethod1.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templatePaymentMethod1.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templatePaymentMethod1);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templatePaymentMethod1);
        });
        setTextGravity(viewController.templatePaymentMethod1);
    }

    public void templatePaymentMethod3_click() {
        setTextBackgroudColor();
        viewController.templatePaymentMethod3.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templatePaymentMethod3.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templatePaymentMethod3);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templatePaymentMethod3);
        });
        setTextGravity(viewController.templatePaymentMethod3);
    }

    public void templateFooter1_click() {
        setTextBackgroudColor();
        viewController.templateFooter1.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter1.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter1);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter1);
        });
        setTextGravity(viewController.templateFooter1);
    }

    public void templateFooter2_click() {
        setTextBackgroudColor();
        viewController.templateFooter2.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter2.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter2);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter2);
        });
        setTextGravity(viewController.templateFooter2);
    }

    public void templateFooter3_click() {
        setTextBackgroudColor();
        viewController.templateFooter3.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter3.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter3);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter3);
        });
        setTextGravity(viewController.templateFooter3);
    }

    public void templateFooter4_click() {
        setTextBackgroudColor();
        viewController.templateFooter4.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter4.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter4);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter4);
        });
        setTextGravity(viewController.templateFooter4);
    }

    public void templateFooter5_click() {
        setTextBackgroudColor();
        viewController.templateFooter5.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter5.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter5);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter5);
        });
        setTextGravity(viewController.templateFooter5);
    }

    public void templateFooter6_click() {
        setTextBackgroudColor();
        viewController.templateFooter6.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter6.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter6);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter6);
        });
        setTextGravity(viewController.templateFooter6);
    }

    public void templateFooter7_click() {
        setTextBackgroudColor();
        viewController.templateFooter7.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter7.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter7);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter7);
        });
        setTextGravity(viewController.templateFooter7);
    }

    public void templateFooter8_click() {
        setTextBackgroudColor();
        viewController.templateFooter8.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter8.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter8);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter8);
        });
        setTextGravity(viewController.templateFooter8);
    }

    public void templateFooter9_click() {
        setTextBackgroudColor();
        viewController.templateFooter9.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter9.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter9);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter9);
        });
        setTextGravity(viewController.templateFooter9);
    }

    public void templateFooter10_click() {
        setTextBackgroudColor();
        viewController.templateFooter10.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateFooter10.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateFooter10);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateFooter10);
        });
        setTextGravity(viewController.templateFooter10);
    }

    public void templateLogo_click() {
        setTextBackgroudColor();
//        viewController.templateLogo.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        viewController.controlKeepLeft.setOnMouseClicked(event -> {
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_click);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            viewController.templateLogoLayout.setAlignment(Pos.TOP_LEFT);
        });
        viewController.controlKeepCenter.setOnMouseClicked(event -> {
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_click);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            viewController.templateLogoLayout.setAlignment(Pos.CENTER);
        });
        viewController.controlKeepRight.setOnMouseClicked(event -> {
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_click);
            viewController.templateLogoLayout.setAlignment(Pos.TOP_RIGHT);
        });
    }

    public void templateTicketBottom_click() {
        setTextBackgroudColor();
        viewController.templateTicketBottom.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        viewController.templateTicketBottom.setTextFill(Color.WHITE);
        setBoldButtonColor(viewController.templateTicketBottom);
        viewController.controlBold.setOnMouseClicked(event -> {
            clickBold(viewController.templateTicketBottom);
        });
        setTextGravity(viewController.templateTicketBottom);
    }

    public void print_test_click() {
        onPrinterTest();
    }

    public void smallsheet_create_click() {
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("Where F_SyncType != '%s'");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        List<SmallSheetFrame> ssfList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.info("???????????????????????????????????????");
            return;
        }
        log.info("?????????????????????????????????" + ssfList.size());
        if (ssfList.size() >= Integer.valueOf(ConfigGeneral.Config_SmallSheetNumber_Value)) {
            ToastUtil.toast("?????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
            log.info("?????????????????????????????????????????????");
            return;
        }
        //???????????????????????????????????????????????????????????????UI????????????????????????????????????????????????????????????????????????????????????????????????????????????
        initRightContent();
        viewController.smallsheet_create.setVisible(false);
        viewController.smallsheet_create.setManaged(false);
        viewController.smallsheet_update.setVisible(false);
        viewController.smallsheet_update.setManaged(false);
        viewController.smallsheet_delete.setVisible(false);
        viewController.smallsheet_delete.setManaged(false);
        viewController.createConfirm.setVisible(true);
        viewController.createConfirm.setManaged(true);
        viewController.createCancle.setVisible(true);
        viewController.createCancle.setManaged(true);
        viewController.useForm.setVisible(false);
        viewController.useForm.setManaged(false);
        viewController.loadingConfigData.setVisible(false);//??????????????????
        viewController.loadingConfigData.setManaged(false);
        viewController.smallsheetToUpdate.setVisible(false);//????????????????????????
        viewController.smallsheetToUpdate.setManaged(false);
        viewController.Mask.setVisible(false);//????????????
        //
        initRightContent();
    }

    public void smallsheet_update_click() {
        onUpdateSmallSheetInUI();
    }

    public void smallsheet_delete_click() {
        onDeleteSmallSheetInUI();
    }

    public void loadingConfigData_click() {
        viewController.showLoadingDialog();
        // ??????????????????
        SmallSheetFrame resetSmallSheetFrame = new SmallSheetFrame();
        resetSmallSheetFrame.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        resetSmallSheetFrame.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, resetSmallSheetFrame)) {
            viewController.closeLoadingDialog();
            ToastUtil.toast("???????????????", ToastUtil.LENGTH_SHORT);
        }
    }

    public void createConfirm_click() {
        onConfirmCreateInUI();
    }

    public void createCancle_click() {
//????????????UI????????????????????????????????????????????????????????????????????????????????????????????????????????????
        viewController.smallsheet_create.setVisible(true);
        viewController.smallsheet_create.setManaged(true);
        viewController.smallsheet_update.setVisible(false);
        viewController.smallsheet_update.setManaged(false);
        viewController.smallsheet_delete.setVisible(true);
        viewController.smallsheet_delete.setManaged(true);
        viewController.useForm.setVisible(true);
        viewController.useForm.setManaged(true);
        viewController.createConfirm.setVisible(false);
        viewController.createConfirm.setManaged(false);
        viewController.createCancle.setVisible(false);
        viewController.createCancle.setManaged(false);
        viewController.smallsheetToUpdate.setVisible(true);//????????????????????????
        viewController.smallsheetToUpdate.setManaged(true);
        viewController.loadingConfigData.setVisible(true);//??????????????????
        viewController.loadingConfigData.setManaged(true);
        viewController.Mask.setVisible(true);//????????????
        //
        if (selectedFrameID != null) {
            initSmallSheetData(Integer.valueOf(String.valueOf(selectedFrameID)));
        }
    }


    public void useForm_click() {
// ??????????????????????????????
        if ((GlobalController.getInstance().getSessionID() == null || !NetworkUtils.isNetworkAvalible())) {
            ToastUtil.toast("?????????????????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
            GlobalController.getInstance().setSessionID(null);
            return;
        }
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setSql("where F_Name = '%s'");
        configGeneral.setConditions(new String[]{"ACTIVE_SMALLSHEET_ID"});
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        configGeneralList.get(0).setReturnObject(1);//...
        configGeneralList.get(0).setValue(String.valueOf(selectedFrameID));
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList.get(0))) {
            ToastUtil.toast("?????????????????????????????????", ToastUtil.LENGTH_SHORT);
        } else {
            currentSmallSheetID = selectedFrameID.intValue();// ????????????????????????
        }

        viewController.showLoadingDialog();
    }


    public void addDesignBottomBlankLineNumber_click() {
        if (viewController.designBottomBlankLine.getText().toString().equals("5")) {
            ToastUtil.toast(SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom, ToastUtil.LENGTH_SHORT);
        } else {
            viewController.designBottomBlankLine.setText(String.valueOf(Integer.valueOf(viewController.designBottomBlankLine.getText().toString()) + 1));
        }
    }

    public void reduceDesignBottomBlankLineNumber_click() {
        if (viewController.designBottomBlankLine.getText().toString().equals("0")) {
            ToastUtil.toast(SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom, ToastUtil.LENGTH_SHORT);
        } else {
            viewController.designBottomBlankLine.setText(String.valueOf(Integer.valueOf(viewController.designBottomBlankLine.getText().toString()) - 1));
        }
    }


    public void smallsheetToUpdate_click() {
        viewController.smallsheet_update.setVisible(true);//????????????????????????
        viewController.smallsheet_update.setManaged(true);
        viewController.cancelUpdate.setVisible(true);//????????????????????????
        viewController.cancelUpdate.setManaged(true);
        viewController.smallsheet_create.setVisible(false);//????????????????????????
        viewController.smallsheet_create.setManaged(false);
        viewController.loadingConfigData.setVisible(false);//??????????????????
        viewController.loadingConfigData.setManaged(false);
        viewController.useForm.setVisible(false);//???????????????????????????
        viewController.useForm.setManaged(false);
        viewController.smallsheetToUpdate.setVisible(false);//????????????????????????
        viewController.smallsheetToUpdate.setManaged(false);
        viewController.Mask.setVisible(false);//????????????
    }

    public void cancelUpdate_click() {
        viewController.smallsheet_update.setVisible(false);//????????????????????????
        viewController.smallsheet_update.setManaged(false);
        viewController.cancelUpdate.setVisible(false);//????????????????????????
        viewController.cancelUpdate.setManaged(false);
        viewController.smallsheet_create.setVisible(true);//????????????????????????
        viewController.smallsheet_create.setManaged(true);
        viewController.loadingConfigData.setVisible(true);//??????????????????
        viewController.loadingConfigData.setManaged(true);
        viewController.smallsheetToUpdate.setVisible(true);//????????????????????????
        viewController.smallsheetToUpdate.setManaged(true);
        viewController.Mask.setVisible(true);//????????????
        viewController.useForm.setVisible(true);//???????????????????????????
        viewController.useForm.setManaged(true);
        showSelectedSmallSheet(SelectPosition);
    }

    private void onConfirmCreateInUI() {
        viewController.showLoadingDialog();

        //????????????UI????????????????????????????????????????????????????????????????????????????????????????????????????????????
        viewController.smallsheet_create.setVisible(true);
        viewController.smallsheet_create.setManaged(true);
        viewController.smallsheet_update.setVisible(false);
        viewController.smallsheet_update.setManaged(false);
        viewController.smallsheet_delete.setVisible(true);
        viewController.smallsheet_delete.setManaged(true);
        viewController.useForm.setVisible(true);
        viewController.useForm.setManaged(true);
        viewController.createConfirm.setVisible(false);
        viewController.createConfirm.setManaged(false);
        viewController.createCancle.setVisible(false);
        viewController.createCancle.setManaged(false);
        viewController.smallsheetToUpdate.setVisible(true);
        viewController.smallsheetToUpdate.setManaged(true);
        viewController.loadingConfigData.setVisible(true);
        viewController.loadingConfigData.setManaged(true);
        viewController.Mask.setVisible(true);

        //???????????????ID???????????????ID????????????????????????ID
        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            return;
        }

        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        if (smallSheetTextPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            return;
        }

        SmallSheetFrame ssf = new SmallSheetFrame();
        if (bmpFrameLogo != null) {
            ssf.setLogo(GeneralUtil.fileToString(bmpFrameLogo));
        }
        List<SmallSheetText> smallSheetTextList = getSmallSheetTextFromView();
        for (int i = 0; i < smallSheetTextList.size(); i++) {
            smallSheetTextList.get(i).setID(maxTextIDInSQLite + i);
            smallSheetTextList.get(i).setFrameId((long) maxTextIDInSQLite);
        }

        ssf.setListSlave1(smallSheetTextList);
        ssf.setReturnObject(1);//...
        ssf.setID(maxFrameIDInSQLite);
        ssf.setCountOfBlankLineAtBottom(Integer.valueOf(viewController.designBottomBlankLine.getText()));
        ssf.setDelimiterToRepeat(viewController.designDelimiterToRepeat.getText());
        ssf.setCreateDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssf)) {
            ToastUtil.toast(smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            viewController.closeLoadingDialog();
            return;
        }
        return;
    }

    private void onDeleteSmallSheetInUI() {
        viewController.smallsheet_delete.setDisable(true);
        System.out.println("????????????ID???" + selectedFrameID);
        if (selectedFrameID != null) {
            // ??????????????????????????????????????????ID
            if (selectedFrameID != Default_SmallSheetID_INPos) {
                viewController.showLoadingDialog();
                //??????????????????????????????????????????
                SmallSheetFrame deleteSSF = new SmallSheetFrame();
                deleteSSF.setID(selectedFrameID);
                deleteSSF.setDelimiterToRepeat("");
                deleteSSF.setReturnObject(1);//...
                //???????????????frame???????????????text???????????????updateSST???ID
                deleteSSF.setSql(" where F_FrameID = '%s' ");
                deleteSSF.setConditions(new String[]{String.valueOf(deleteSSF.getID())});
                List<SmallSheetText> deleteSST = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, deleteSSF);

                if (selectedFrameID != null) {
                    deleteSSF.setListSlave1(deleteSST);
                    deleteSSF.setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
                } else {
                    viewController.closeLoadingDialog();
                    ToastUtil.toast("?????????????????????????????????", ToastUtil.LENGTH_SHORT);
                    viewController.smallsheet_delete.setDisable(false);
                    return;
                }
                //???????????????SyncType???SyncDatetime
                smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done);
                if (!smallSheetSQLiteBO.deleteAsync(BaseSQLiteBO.INVALID_CASE_ID, deleteSSF)) {
                    //??????????????????
                    viewController.closeLoadingDialog();
                    ToastUtil.toast("????????????????????????!", ToastUtil.LENGTH_SHORT);
                    viewController.smallsheet_delete.setDisable(false);
                    return;
                }
            } else {
                ToastUtil.toast("????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                viewController.smallsheet_delete.setDisable(false);
            }
        } else {
            ToastUtil.toast("???????????????????????????????????????", ToastUtil.LENGTH_SHORT);
            viewController.smallsheet_delete.setDisable(false);
        }
    }

    private void onUpdateSmallSheetInUI() {
        if (selectedFrameID != null) {
            viewController.showLoadingDialog();
            //??????UI????????????????????????????????????
            SmallSheetFrame updateSSF = new SmallSheetFrame();
            updateSSF.setID(selectedFrameID);
            updateSSF.setReturnObject(1);//...
            updateSSF.setCountOfBlankLineAtBottom(Integer.valueOf(viewController.designBottomBlankLine.getText().toString()));
            updateSSF.setDelimiterToRepeat(viewController.designDelimiterToRepeat.getText().toString());
            if (bmpFrameLogo != null) {
                updateSSF.setLogo(GeneralUtil.fileToString(bmpFrameLogo));
            } else {
                updateSSF.setLogo("");
            }
            List<SmallSheetText> updateSST = getSmallSheetTextFromView();

            //???????????????frame???????????????text???????????????updateSST???ID
            updateSSF.setSql(" where F_FrameID = '%s' ");
            updateSSF.setConditions(new String[]{String.valueOf(updateSSF.getID())});
            List<SmallSheetText> updateTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, updateSSF);
            for (int i = 0; i < updateSST.size(); i++) {
                updateSST.get(i).setID(updateTextList.get(i).getID());
                updateSST.get(i).setFrameId((long) updateSSF.getID());
            }
            updateSSF.setListSlave1(updateSST);
            updateSSF.setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
            //
            smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
            smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync);
            if (!smallSheetSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, updateSSF)) {
//                        ToastUtil.toast(appApplication, "????????????????????????!", ToastUtil.LENGTH_SHORT);
                ToastUtil.toast(smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
//                closeLoadingDialog();
                viewController.closeLoadingDialog();
            }
        } else {
            ToastUtil.toast("???????????????????????????????????????", ToastUtil.LENGTH_SHORT);
        }
        viewController.loadingConfigData.setVisible(true);
        viewController.loadingConfigData.setManaged(true);
        viewController.smallsheet_create.setVisible(true);
        viewController.smallsheet_create.setManaged(true);
        viewController.smallsheetToUpdate.setVisible(true);
        viewController.smallsheetToUpdate.setManaged(true);
        viewController.cancelUpdate.setVisible(false);
        viewController.cancelUpdate.setManaged(false);
        viewController.smallsheet_update.setVisible(false);
        viewController.smallsheet_update.setManaged(false);
        viewController.useForm.setVisible(true);
        viewController.useForm.setManaged(true);
        viewController.Mask.setVisible(true);
    }

    /**
     * ????????????????????????????????????????????????
     */
    private boolean checkTextContentLength(String s) {
        if (s.length() < MAX_LENGTH_Content) {
            return true;
        }
        return false;
    }

    /**
     * ??????????????????????????????????????????
     */
    private void onPrinterTest() {
        if (checkTextContentLength(viewController.templateHeader.getText())
                && checkTextContentLength(viewController.templateFooter1.getText())
                && checkTextContentLength(viewController.templateFooter2.getText())
                && checkTextContentLength(viewController.templateFooter3.getText())
                && checkTextContentLength(viewController.templateFooter4.getText())
                && checkTextContentLength(viewController.templateFooter5.getText())
                && checkTextContentLength(viewController.templateFooter6.getText())
                && checkTextContentLength(viewController.templateFooter7.getText())
                && checkTextContentLength(viewController.templateFooter8.getText())
                && checkTextContentLength(viewController.templateFooter9.getText())
                && checkTextContentLength(viewController.templateFooter10.getText())
                && checkTextContentLength(viewController.templateTicketBottom.getText())) {
            try {
                /**
                 * ???????????????????????????????????????
                 */
                //??????logo??????????????????????????????????????????????????????
                if ("?????????".equals(viewController.designIfTopLogo.getText())) {
                    AidlUtil.getInstance().printMemoryBitmap(bmpFrameLogo.getAbsolutePath());
                    AidlUtil.getInstance().printDivider(viewController.designDelimiterToRepeat.getText());
                }
                if (!"".equals(viewController.templateHeader.getText())) {
                    AidlUtil.getInstance().printText(viewController.templateHeader.getText().toString(), (int) viewController.templateHeader.getFont().getSize(), "Bold".equals(viewController.templateHeader.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateHeader.getAlignment().toString()), false);
                    AidlUtil.getInstance().printDivider(viewController.designDelimiterToRepeat.getText().toString());
                }
                AidlUtil.getInstance().printText(viewController.templateDocumentNumber.getText(), (int) viewController.templateDocumentNumber.getFont().getSize(), "Bold".equals(viewController.templateDocumentNumber.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateDocumentNumber.getAlignment().toString()), false);
                AidlUtil.getInstance().printText(viewController.templateDate.getText(), (int) viewController.templateDate.getFont().getSize(), "Bold".equals(viewController.templateDate.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateDate.getAlignment().toString()), false);
                AidlUtil.getInstance().printDivider(viewController.designDelimiterToRepeat.getText());

                String[] goods_attrs = new String[]{"????????????", "??????", "??????"};
                String[] first_goods = new String[]{"??????A", "x1", "1020"};
                String[] second_goods = new String[]{"??????B", "x5", "5500"};
                String[] third_goods = new String[]{"??????C", "x3", "3"};
                AidlUtil.getInstance().printTable(goods_attrs);
                AidlUtil.getInstance().printTable(first_goods);
                AidlUtil.getInstance().printTable(second_goods);
                AidlUtil.getInstance().printTable(third_goods);

                AidlUtil.getInstance().printDivider(viewController.designDelimiterToRepeat.getText());

                AidlUtil.getInstance().printText(viewController.templateTotalMoney.getText(), (int) viewController.templateTotalMoney.getFont().getSize(), "Bold".equals(viewController.templateTotalMoney.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateTotalMoney.getAlignment().toString()), false);
                AidlUtil.getInstance().printText(viewController.templatePaymentMethod.getText(), (int) viewController.templatePaymentMethod.getFont().getSize(), "Bold".equals(viewController.templatePaymentMethod.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templatePaymentMethod.getAlignment().toString()), false);
                AidlUtil.getInstance().printText(viewController.templateDiscount.getText(), (int) viewController.templateDiscount.getFont().getSize(), "Bold".equals(viewController.templateDiscount.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateDiscount.getAlignment().toString()), false);
                AidlUtil.getInstance().printText(viewController.templatePayable.getText(), (int) viewController.templatePayable.getFont().getSize(), "Bold".equals(viewController.templatePayable.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templatePayable.getAlignment().toString()), false);
                AidlUtil.getInstance().printText(viewController.templatePaymentMethod1.getText(), (int) viewController.templatePaymentMethod1.getFont().getSize(), "Bold".equals(viewController.templatePaymentMethod1.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templatePaymentMethod1.getAlignment().toString()), false);
                AidlUtil.getInstance().printText(viewController.templatePaymentMethod3.getText(), (int) viewController.templatePaymentMethod3.getFont().getSize(), "Bold".equals(viewController.templatePaymentMethod3.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templatePaymentMethod3.getAlignment().toString()), false);
                AidlUtil.getInstance().printDivider(viewController.designDelimiterToRepeat.getText());
                if (viewController.templateFooter1.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter1.getText().toString(), (int) viewController.templateFooter1.getFont().getSize(), "Bold".equals(viewController.templateFooter1.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter1.getAlignment().toString()), false);
                }
                if (viewController.templateFooter2.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter2.getText().toString(), (int) viewController.templateFooter2.getFont().getSize(), "Bold".equals(viewController.templateFooter2.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter2.getAlignment().toString()), false);
                }
                if (viewController.templateFooter3.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter3.getText().toString(), (int) viewController.templateFooter3.getFont().getSize(), "Bold".equals(viewController.templateFooter3.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter3.getAlignment().toString()), false);
                }
                if (viewController.templateFooter4.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter4.getText().toString(), (int) viewController.templateFooter4.getFont().getSize(), "Bold".equals(viewController.templateFooter4.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter4.getAlignment().toString()), false);
                }
                if (viewController.templateFooter5.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter5.getText().toString(), (int) viewController.templateFooter5.getFont().getSize(), "Bold".equals(viewController.templateFooter5.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter5.getAlignment().toString()), false);
                }
                if (viewController.templateFooter6.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter6.getText().toString(), (int) viewController.templateFooter6.getFont().getSize(), "Bold".equals(viewController.templateFooter6.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter6.getAlignment().toString()), false);
                }
                if (viewController.templateFooter7.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter7.getText().toString(), (int) viewController.templateFooter7.getFont().getSize(), "Bold".equals(viewController.templateFooter7.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter7.getAlignment().toString()), false);
                }
                if (viewController.templateFooter8.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter8.getText().toString(), (int) viewController.templateFooter8.getFont().getSize(), "Bold".equals(viewController.templateFooter8.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter8.getAlignment().toString()), false);
                }
                if (viewController.templateFooter9.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter9.getText().toString(), (int) viewController.templateFooter9.getFont().getSize(), "Bold".equals(viewController.templateFooter9.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter9.getAlignment().toString()), false);
                }
                if (viewController.templateFooter10.isVisible()) {
                    AidlUtil.getInstance().printText(viewController.templateFooter10.getText().toString(), (int) viewController.templateFooter10.getFont().getSize(), "Bold".equals(viewController.templateFooter10.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateFooter10.getAlignment().toString()), false);
                }
                AidlUtil.getInstance().printText(viewController.templateTicketBottom.getText(), (int) viewController.templateTicketBottom.getFont().getSize(), "Bold".equals(viewController.templateTicketBottom.getFont().getStyle()), SmallSheetText.EnumSmallSheetTextGravity.getIndex(viewController.templateTicketBottom.getAlignment().toString()), false);
                // ???????????????????????????????????????????????????0????????????????????????
                AidlUtil.getInstance().linewrap(viewController.designBottomBlankLine.getText().trim().equals("") ? 0 : Integer.valueOf(viewController.designBottomBlankLine.getText().toString()));
                AidlUtil.getInstance().cutPaper();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.toast("??????????????????????????????????????????????????????" + MAX_LENGTH_Content + "?????????????????????" + MAX_LENGTH_Content + "??????????????????", ToastUtil.LENGTH_SHORT);
        }
    }

    /**
     * ??????????????????logo
     */
    private void onClickSelectLogo() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/choosePrinterSizeDialog.fxml"));

        JFXAlert choosePrinterSizeDialog = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
        choosePrinterSizeDialog.initModality(Modality.APPLICATION_MODAL);
        choosePrinterSizeDialog.setOverlayClose(false);
        try {
            choosePrinterSizeDialog.setContent((FlowPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        choosePrinterSizeDialog.show();

        ChoosePrinterSizeDialogViewController choosePrinterSizeDialogViewController = loader.getController();
        choosePrinterSizeDialogViewController.setAlert(choosePrinterSizeDialog);
        //
        selected_no_logo = choosePrinterSizeDialogViewController.selected1;
        selected_from_album = choosePrinterSizeDialogViewController.selected2;
        //
        choosePrinterSizeDialogViewController.select1_tv.setText("???");
        choosePrinterSizeDialogViewController.select2_tv.setText("???????????????");
        //
        if ("???".equals(viewController.designIfTopLogo.getText())) {
            selected_no_logo.setVisible(true);
            selected_no_logo.setManaged(true);
            selected_from_album.setVisible(false);
            selected_from_album.setManaged(false);
            viewController.templateLogo.setVisible(false);
            viewController.templateLogo.setManaged(false);
            bmpFrameLogo = null;
            viewController.templateLogo.setImage(null);
        } else if ("?????????".equals(viewController.designIfTopLogo.getText())) {
            selected_no_logo.setVisible(false);
            selected_no_logo.setManaged(false);
            selected_from_album.setVisible(true);
            selected_from_album.setManaged(true);
            viewController.templateLogo.setVisible(true);
            viewController.templateLogo.setManaged(true);
        }

        choosePrinterSizeDialogViewController.select1.setOnMouseClicked(event -> {
            viewController.templateLogoLayout.setVisible(false);
            viewController.templateLogoLayout.setManaged(false);
            viewController.templateLogo.setVisible(false);
            viewController.templateLogo.setManaged(false);
            viewController.designIfTopLogo.setText("???");
            bmpFrameLogo = null;
            viewController.templateLogo.setImage(null);
            selected_no_logo.setVisible(true);
            selected_no_logo.setManaged(true);
            selected_from_album.setVisible(false);
            selected_from_album.setManaged(false);
            choosePrinterSizeDialog.close();
        });

        // ?????????????????????
        choosePrinterSizeDialogViewController.select2.setOnMouseClicked(event -> {
            int REQUEST_EXTERNAL_STORAGE = 1;

            viewController.templateLogoLayout.setVisible(true);
            viewController.templateLogoLayout.setManaged(true);
            viewController.templateLogo.setVisible(true);
            viewController.templateLogo.setManaged(true);
            viewController.designIfTopLogo.setText("?????????");
            selected_no_logo.setVisible(false);
            selected_no_logo.setManaged(true);
            selected_from_album.setVisible(true);
            selected_from_album.setManaged(true);

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                    new FileChooser.ExtensionFilter("GIF", "*.gif"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp")
            );
            File file = fileChooser.showOpenDialog(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
            if (file != null) {
                showCutImageDialog(file, choosePrinterSizeDialog);
            }
        });
        choosePrinterSizeDialogViewController.cancel.setOnMouseClicked(event -> choosePrinterSizeDialog.close());
    }

    private void showCutImageDialog(File chooceImage, JFXAlert choosePrinterSizeDialog) {
        FXMLLoader cutImageLoader = new FXMLLoader();
        cutImageLoader.setLocation(getClass().getResource("/ui/cutImageDialog.fxml"));

        JFXAlert cutImageAlert = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
        cutImageAlert.initModality(Modality.APPLICATION_MODAL);
        cutImageAlert.setOverlayClose(false);
        try {
            cutImageAlert.setContent((BorderPane) cutImageLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cutImageAlert.show();

        CutImageDialog controller = cutImageLoader.getController();
        controller.onCreate();
        controller.setAlert(cutImageAlert);
        controller.setListener((x, y, width, height) -> {
            try {
                BufferedImage bufferedImage = ImageIO.read(chooceImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                boolean write = false;
                if (bufferedImage.getWidth() >= 580 && bufferedImage.getHeight() >= 380) {
                    //??????????????????????????????????????????????????????x,y??????????????????????????????????????????????????????????????????x???y?????????
                    x = x / (600.0 / bufferedImage.getWidth()); //?????????600???dialog?????????
                    y = y / (600.0 / bufferedImage.getHeight()); //?????????600???dialog?????????
                    //????????????????????????
                    width = width / (580.0 / bufferedImage.getWidth());//?????????580?????????????????????
                    height = height / (380.0 / bufferedImage.getHeight());//?????????380?????????????????????
                    //????????????????????????????????????????????????????????????
                    if (width >= 580) {
                        x = 0;
                        y = 0;
                        width = bufferedImage.getWidth();
                        height = bufferedImage.getHeight();
                    }
                    //????????????
                    bufferedImage = bufferedImage.getSubimage((int) x, (int) y, (int) width, (int) height);
                    //?????????????????????????????????????????????
                    baos = new ByteArrayOutputStream();
                    write = ImageIO.write(bufferedImage, "JPEG", baos);
                } else {
                    write = ImageIO.write(bufferedImage, "JPEG", baos);
                }
                if (write) {
                    byte[] bytes = baos.toByteArray();
                    String encodeToString = Base64.getEncoder().encodeToString(bytes);
                    System.out.println(encodeToString.length());
                    if (encodeToString.length() > SmallSheetFrame.SmallSheetLogoVolume) {
                        ToastUtil.toast("????????????");
                        bmpFrameLogo = null;
                        viewController.templateLogo.setImage(null);
                        viewController.templateLogoLayout.setVisible(false);
                        viewController.templateLogoLayout.setManaged(false);
                        viewController.designIfTopLogo.setText("???");
                    } else {
                        File outputFile = new File(IMAGE_FILE_LOCATION);
                        if (ImageIO.write(bufferedImage, "JPEG", outputFile)) {
                            FileInputStream inputStream = new FileInputStream(outputFile);
                            viewController.templateLogo.setImage(new Image(inputStream));
                            inputStream.close();
                            bmpFrameLogo = outputFile;
                        } else {
                            ToastUtil.toast("???????????????????????????");
                        }
                    }
                } else {
                    System.out.println("????????????????????????????????????");
                }

                cutImageAlert.close();
                choosePrinterSizeDialog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        try {
            FileInputStream inputStream = new FileInputStream(chooceImage);
            controller.image.setImage(new Image(inputStream));
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????????????????????????????????, ?????????????????????
     *
     * @param footer          ??????
     * @param footer_editText ??????????????????
     */
    private void setFooterLayoutIsVisible(BorderPane footer, TextField footer_editText) {
        footer.setVisible(true);
        footer.setManaged(true);
    }

    /**
     * ???????????????????????????????????????????????????????????? ?????????????????????TextView ??? Layout, ?????????????????????
     *
     * @param textView ???????????????TextView
     * @param layout   ???????????????layout
     */
//    @TargetApi(Build.VERSION_CODES.M)
    private void selectGoodsListLayout(Label textView, Label layout) {
        setTextBackgroudColor();
        if (layout != null) {
            textView.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
            layout.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        }
        viewController.showControlTextSize.setText((int) textView.getFont().getSize() + "");
        viewController.controlKeepLeft.setDisable(false);
        viewController.controlKeepCenter.setDisable(false);
        viewController.controlKeepRight.setDisable(false);
        viewController.controlTextSize.setDisable(false);
        viewController.left_shift.setDisable(false);
        viewController.right_shift.setDisable(false);
        viewController.controlBold.setDisable(false);
    }

    /**
     * ???????????????text, ??????????????????????????????, ??????????????????????????????, ???????????????????????????, ???????????????, ????????????
     *
     * @param textView ?????????text
     */
//    @TargetApi(Build.VERSION_CODES.M)
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void clickBold(Label textView) {
        if (!"Bold".equals(textView.getFont().getStyle())) {
//            controlBold.setBackgroundColor(Color.YELLOW);
            viewController.controlBold.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            viewController.controlBold.setTextFill(Color.BLUE);
            textView.setFont(Font.font("", FontWeight.BOLD, textView.getFont().getSize()));
            textView.getFont().equals(FontWeight.BOLD);
        } else {
//            controlBold.setBackground(background);
            viewController.controlBold.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            viewController.controlBold.setTextFill(Color.BLACK);
            textView.setFont(Font.font("", FontWeight.NORMAL, textView.getFont().getSize()));
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     */
    private void initRightContent() {
        viewController.designIfTopLogo.setText("???");
        viewController.templateLogoLayout.setVisible(false);
        viewController.templateLogoLayout.setManaged(false);
        viewController.designHeader.setText(defaultText);
        viewController.templateHeader.setText(viewController.designHeader.getText());
        viewController.designFooter1.setText(defaultText);
        viewController.templateFooter1.setText(viewController.designFooter1.getText());
        viewController.designTicketBottom.setText(defaultText);
        viewController.templateTicketBottom.setText(viewController.designTicketBottom.getText());
        bmpFrameLogo = null;
        //...????????????????????????????????????
        viewController.designFooter2.setText(defaultText);
        viewController.templateFooter2.setText(viewController.designFooter2.getText());
        viewController.designFooter3.setText(defaultText);
        viewController.templateFooter3.setText(viewController.designFooter3.getText());
        viewController.designFooter4.setText(defaultText);
        viewController.templateFooter4.setText(viewController.designFooter4.getText());
        viewController.designFooter5.setText(defaultText);
        viewController.templateFooter5.setText(viewController.designFooter5.getText());
        viewController.designFooter6.setText(defaultText);
        viewController.templateFooter6.setText(viewController.designFooter6.getText());
        viewController.designFooter7.setText(defaultText);
        viewController.templateFooter7.setText(viewController.designFooter7.getText());
        viewController.designFooter8.setText(defaultText);
        viewController.templateFooter8.setText(viewController.designFooter8.getText());
        viewController.designFooter9.setText(defaultText);
        viewController.templateFooter9.setText(viewController.designFooter9.getText());
        viewController.designFooter10.setText(defaultText);
        viewController.templateFooter10.setText(viewController.designFooter10.getText());

        viewController.showControlTextSize.setText("30");

        Font font = new Font(MAX_Length_EditText);
        viewController.templateHeader.setFont(font);
        viewController.templateDocumentNumber.setFont(font);
        viewController.templateDate.setFont(font);
        viewController.templateTotalMoney.setFont(font);
        viewController.templatePaymentMethod.setFont(font);
        viewController.templateDiscount.setFont(font);
        viewController.templatePayable.setFont(font);
        viewController.templatePaymentMethod1.setFont(font);
        viewController.templatePaymentMethod3.setFont(font);
        viewController.templateFooter1.setFont(font);
        viewController.templateFooter2.setFont(font);
        viewController.templateFooter3.setFont(font);
        viewController.templateFooter4.setFont(font);
        viewController.templateFooter5.setFont(font);
        viewController.templateFooter6.setFont(font);
        viewController.templateFooter7.setFont(font);
        viewController.templateFooter8.setFont(font);
        viewController.templateFooter9.setFont(font);
        viewController.templateFooter10.setFont(font);
        viewController.templateTicketBottom.setFont(font);
    }

    /**
     * ?????????????????????UI??????TextView???Text????????????????????????????????????????????????
     */
    private void initTemplateSmallSheet(List<SmallSheetText> smallSheetTextList) {
        setTemplateContent(viewController.templateTotalMoney, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()));
        setTemplateContent(viewController.templatePaymentMethod, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()));
        setTemplateContent(viewController.templateDiscount, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()));
        setTemplateContent(viewController.templatePayable, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()));
        setTemplateContent(viewController.templatePaymentMethod1, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()));
        setTemplateContent(viewController.templatePaymentMethod3, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()));
        setTemplateContent(viewController.templateHeader, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()));
        setTemplateContent(viewController.templateFooter1, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()));
        setTemplateContent(viewController.templateFooter2, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()));
        setTemplateContent(viewController.templateFooter3, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()));
        setTemplateContent(viewController.templateFooter4, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()));
        setTemplateContent(viewController.templateFooter5, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()));
        setTemplateContent(viewController.templateFooter6, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()));
        setTemplateContent(viewController.templateFooter7, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()));
        setTemplateContent(viewController.templateFooter8, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()));
        setTemplateContent(viewController.templateFooter9, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()));
        setTemplateContent(viewController.templateFooter10, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()));
        setTemplateContent(viewController.templateTicketBottom, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()));
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????
     */
    private void showSelectedSmallSheetInUI(SmallSheetFrame smallSheetFrame, List<SmallSheetText> select_smallSheetTextList) {
        if (smallSheetFrame != null) {
            if (smallSheetFrame.getLogo() == null || "".equals(smallSheetFrame.getLogo())) {
                viewController.templateLogoLayout.setVisible(false);
                viewController.templateLogoLayout.setManaged(false);
                viewController.designIfTopLogo.setText("???");
                viewController.templateLogo.setImage(null);
                bmpFrameLogo = null;
            } else {
                viewController.templateLogoLayout.setVisible(true);
                viewController.templateLogoLayout.setManaged(true);
                viewController.designIfTopLogo.setText("?????????");
                bmpFrameLogo = GeneralUtil.stringToFile(smallSheetFrame.getLogo(), IMAGE_FILE_LOCATION);
                if (bmpFrameLogo != null) {
                    try {
                        FileInputStream inputStream = new FileInputStream(bmpFrameLogo);
                        Image image = new Image(inputStream);
                        viewController.templateLogo.setImage(image);
                        viewController.templateLogo.setVisible(true);
                        viewController.templateLogo.setManaged(true);
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ImageIO.write(ImageIO.read(bmpFrameLogo), "JPEG", new File(IMAGE_FILE_LOCATION));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            viewController.controlBold.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            viewController.controlBold.setTextFill(Color.BLACK);
            // TODO: 2020/7/7 ??????
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            setTextBackgroudColor();

            viewController.designDelimiterToRepeat.setText(smallSheetFrame.getDelimiterToRepeat());
            viewController.designBottomBlankLine.setText(String.valueOf(smallSheetFrame.getCountOfBlankLineAtBottom()));

            if (select_smallSheetTextList.size() > 0) {
                setDesignContent(viewController.designHeader, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()));
                setDesignContent(viewController.designFooter1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()));
                setDesignContent(viewController.designFooter2, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()));//???content?????????????????????????????????????????????
                setDesignContent(viewController.designFooter3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()));
                setDesignContent(viewController.designFooter4, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()));
                setDesignContent(viewController.designFooter5, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()));
                setDesignContent(viewController.designFooter6, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()));
                setDesignContent(viewController.designFooter7, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()));
                setDesignContent(viewController.designFooter8, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()));
                setDesignContent(viewController.designFooter9, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()));
                setDesignContent(viewController.designFooter10, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()));
                setDesignContent(viewController.designTicketBottom, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()));

                setTemplateStyle(viewController.templateHeader, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()));
                setTemplateStyle(viewController.templateDocumentNumber, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()));
                setTemplateStyle(viewController.templateDate, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()));
                setTemplateStyle(viewController.templateTotalMoney, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()));
                setTemplateStyle(viewController.templatePaymentMethod, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()));
                setTemplateStyle(viewController.templateDiscount, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()));
                setTemplateStyle(viewController.templatePayable, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()));
                setTemplateStyle(viewController.templatePaymentMethod1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()));
                setTemplateStyle(viewController.templatePaymentMethod3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()));
                setTemplateStyle(viewController.templateFooter1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()));
                setTemplateStyle(viewController.templateFooter2, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()));
                setTemplateStyle(viewController.templateFooter3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()));
                setTemplateStyle(viewController.templateFooter4, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()));
                setTemplateStyle(viewController.templateFooter5, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()));
                setTemplateStyle(viewController.templateFooter6, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()));
                setTemplateStyle(viewController.templateFooter7, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()));
                setTemplateStyle(viewController.templateFooter8, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()));
                setTemplateStyle(viewController.templateFooter9, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()));
                setTemplateStyle(viewController.templateFooter10, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()));
                setTemplateStyle(viewController.templateTicketBottom, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.getIndex(SmallSheetText.show_Ticket_Bottom)));

                setContentIsBold(viewController.templateHeader, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()));
                setContentIsBold(viewController.templateDocumentNumber, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()));
                setContentIsBold(viewController.templateDate, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()));
                setContentIsBold(viewController.templateTotalMoney, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()));
                setContentIsBold(viewController.templatePaymentMethod, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()));
                setContentIsBold(viewController.templateDiscount, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()));
                setContentIsBold(viewController.templatePayable, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()));
                setContentIsBold(viewController.templatePaymentMethod1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()));
                setContentIsBold(viewController.templatePaymentMethod3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()));
                setContentIsBold(viewController.templateFooter1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()));
                setContentIsBold(viewController.templateFooter2, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()));
                setContentIsBold(viewController.templateFooter3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()));
                setContentIsBold(viewController.templateFooter4, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()));
                setContentIsBold(viewController.templateFooter5, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()));
                setContentIsBold(viewController.templateFooter6, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()));
                setContentIsBold(viewController.templateFooter7, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()));
                setContentIsBold(viewController.templateFooter8, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()));
                setContentIsBold(viewController.templateFooter9, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()));
                setContentIsBold(viewController.templateFooter10, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()));
                setContentIsBold(viewController.templateTicketBottom, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()));
            } else {
                ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
            }
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param editText       ???????????????????????????
     * @param smallSheetText ??????????????????????????????Text
     */
    private void setDesignContent(TextField editText, SmallSheetText smallSheetText) {
        if (smallSheetText.getContent() == null || "".equals(smallSheetText.getContent())) {
            editText.setText("");
        } else {
            editText.setText(smallSheetText.getContent());
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param textView       ???????????????????????????
     * @param smallSheetText ??????????????????????????????Text
     */
    private void setTemplateContent(Label textView, SmallSheetText smallSheetText) {
        if (smallSheetText.getContent() == null || "".equals(smallSheetText.getContent())) {
            textView.setText("");
        } else {
            textView.setText(smallSheetText.getContent());
        }
    }

    /**
     * ???????????????????????????
     *
     * @param textView       ??????????????????????????????
     * @param smallSheetText ??????????????????????????????Text
     */
    private void setTemplateStyle(Label textView, SmallSheetText smallSheetText) {
        textView.setFont(new Font(smallSheetText.getSize()));
        log.info("???????????????" + smallSheetText.getSize());
        if (smallSheetText.getGravity() == GRAVITY_CENTRE) {
            textView.setAlignment(javafx.geometry.Pos.CENTER);
        } else if (smallSheetText.getGravity() == GRAVITY_LEFT) {
            textView.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        } else if (smallSheetText.getGravity() == GRAVITY_RIGHT) {
            textView.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        }
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param textView       ????????????
     * @param smallSheetText ??????????????????????????????Text
     */
    private void setContentIsBold(Label textView, SmallSheetText smallSheetText) {
        if (smallSheetText.getBold() == 1) {
            textView.setFont(Font.font("", FontWeight.BOLD, smallSheetText.getSize()));
        } else {
            textView.setFont(Font.font("", FontWeight.NORMAL, smallSheetText.getSize()));
        }
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param editText
     * @param textView
     */
    public void setFooterTextChange(TextField editText, Label textView, BorderPane relativeLayout) {
        if ("".equals(editText.getText()) || null == editText.getText()) {
            // ???????????????View??????,?????????text??????????????????,??????????????????????????????????????????stack???????????????????????????????????????.
            textView.setVisible(false);
            textView.setManaged(false);
            relativeLayout.setVisible(false);
            relativeLayout.setManaged(false);
        } else {
            textView.setVisible(true);
            textView.setManaged(true);
            textView.setText(editText.getText());
            // ??????????????????????????????????????????
            setFooterLayoutIsVisible(relativeLayout, editText);
        }
    }

    /**
     * ???????????????????????????
     */
    private void showSeekBarDialog(final Label set, final Label target) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/reserveDialog.fxml"));

        JFXAlert alert = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        try {
            alert.setContent((BorderPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        alert.show();

        FontSizeSeeBarDialogController seeBarDialogController = loader.getController();
        seeBarDialogController.setAlert(alert);
        seeBarDialogController.setListener(() -> {
            target.setFont(new Font(target.getFont().getName(), target.getFont().getSize() + seeBarDialogController.slider.getValue()));
            set.setText(String.valueOf(target.getFont().getSize()));
            alert.close();
        });
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @param text
     */
    private void setTextGravity(final Label text) {
        viewController.showControlTextSize.setText((int) text.getFont().getSize() + "");
        viewController.controlTextSize.setOnMouseClicked(event -> {
            showSeekBarDialog(viewController.showControlTextSize, text);
        });
        setBoldButtonColor(text);
        if (text.getAlignment() == Pos.TOP_LEFT) {
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_click);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//            viewController. controlKeepRight.setImageResource(R.drawable.right_align_unclick);
        }
        if (text.getAlignment() == Pos.CENTER) {
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_click);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_unclick);
        }
        if (text.getAlignment() == Pos.TOP_RIGHT) {
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_click);
        }
        viewController.controlKeepLeft.setOnMouseClicked(event -> {
            text.setText(text.getText().replace(" ", ""));
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_click);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            text.setAlignment(Pos.TOP_LEFT);
        });
        viewController.controlKeepCenter.setOnMouseClicked(event -> {
            text.setText(text.getText().replace(" ", ""));
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_click);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            text.setAlignment(Pos.CENTER);
        });
        viewController.controlKeepRight.setOnMouseClicked(event -> {
            text.setText(text.getText().replace(" ", ""));
//            viewController.controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
//            viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//            viewController.controlKeepRight.setImageResource(R.drawable.right_align_click);
            text.setAlignment(Pos.TOP_RIGHT);
        });

        viewController.left_shift.setOnMouseClicked(event -> {
            if (text.getAlignment() == Pos.CENTER || text.getAlignment() == Pos.TOP_RIGHT) {//????????????????????????
                switch (text.getId()) {
                    case "templateDocumentNumber":
                        left_shift_blank_number[1]++;
                        log.info("???????????????" + left_shift_blank_number[1]);
                        moveToLeft(text);
                        break;
                    case "templateHeader":
                        left_shift_blank_number[0]++;
                        log.info("???????????????" + left_shift_blank_number[0]);
                        moveToLeft(text);
                        break;
                    case "templateDate":
                        left_shift_blank_number[2]++;
                        log.info("???????????????" + left_shift_blank_number[2]);
                        moveToLeft(text);
                        break;
                    case "total_money":
                        left_shift_blank_number[3]++;
                        log.info("???????????????" + left_shift_blank_number[3]);
                        moveToLeft(text);
                        break;
                    case "templatePaymentMethod":
                        left_shift_blank_number[4]++;
                        log.info("???????????????" + left_shift_blank_number[4]);
                        moveToLeft(text);
                        break;
                    case "templateDiscount":
                        left_shift_blank_number[5]++;
                        log.info("???????????????" + left_shift_blank_number[5]);
                        moveToLeft(text);
                        break;
                    case "templatePayable":
                        left_shift_blank_number[6]++;
                        log.info("???????????????" + left_shift_blank_number[6]);
                        moveToLeft(text);
                        break;
                    case "templatePaymentMethod1":
                        left_shift_blank_number[7]++;
                        log.info("???????????????" + left_shift_blank_number[7]);
                        moveToLeft(text);
                        break;
//                        case viewController."templatePaymentMethod"2:
//                            left_shift_blank_number[8]++;
//                            log.info("???????????????" + left_shift_blank_number[8]);
//                            text.setText(text.getText().toString() + " ");
//                            break;
                    case "templatePaymentMethod3":
                        left_shift_blank_number[8]++;
                        log.info("???????????????" + left_shift_blank_number[8]);
                        moveToLeft(text);
                        break;
                    case "templateFooter1":
                        left_shift_blank_number[9]++;
                        log.info("???????????????" + left_shift_blank_number[9]);
                        moveToLeft(text);
                        break;
                    case "templateFooter2":
                        left_shift_blank_number[10]++;
                        log.info("???????????????" + left_shift_blank_number[10]);
                        moveToLeft(text);
                        break;
                    case "templateFooter3":
                        left_shift_blank_number[11]++;
                        log.info("???????????????" + left_shift_blank_number[11]);
                        moveToLeft(text);
                        break;
                    case "templateFooter4":
                        left_shift_blank_number[12]++;
                        log.info("???????????????" + left_shift_blank_number[12]);
                        moveToLeft(text);
                        break;
                    case "templateFooter5":
                        left_shift_blank_number[13]++;
                        log.info("???????????????" + left_shift_blank_number[13]);
                        moveToLeft(text);
                        break;
                    case "templateFooter6":
                        left_shift_blank_number[14]++;
                        log.info("???????????????" + left_shift_blank_number[14]);
                        moveToLeft(text);
                        break;
                    case "templateFooter7":
                        left_shift_blank_number[15]++;
                        log.info("???????????????" + left_shift_blank_number[15]);
                        moveToLeft(text);
                        break;
                    case "templateFooter8":
                        left_shift_blank_number[16]++;
                        log.info("???????????????" + left_shift_blank_number[16]);
                        moveToLeft(text);
                        break;
                    case "templateFooter9":
                        left_shift_blank_number[17]++;
                        log.info("???????????????" + left_shift_blank_number[17]);
                        moveToLeft(text);
                        break;
                    case "templateFooter10":
                        left_shift_blank_number[18]++;
                        log.info("???????????????" + left_shift_blank_number[18]);
                        moveToLeft(text);
                        break;
                    case "templateTicketBottom":
                        left_shift_blank_number[19]++;
                        log.info("???????????????" + left_shift_blank_number[19]);
                        moveToLeft(text);
                        break;
                    default:
                        break;
                }

            } else if (text.getAlignment() == Pos.TOP_LEFT) {//?????????????????????
                if (text.getText().toString().indexOf(" ") == -1) {//?????????????????????????????????????????????text??????????????????????????????text???????????????????????????????????????
                } else {//??????!=-1??????????????????text???????????????text?????????????????????????????????????????????????????????text???????????????????????????
                    switch (text.getId()) {
                        case "templateHeader":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[0]--;
                            log.info("???????????????" + right_shift_blank_number[0]);
                            break;
                        case "templateDocumentNumber":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[1]--;
                            log.info("???????????????" + right_shift_blank_number[1]);
                            break;
                        case "templateDate":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[2]--;
                            log.info("???????????????" + right_shift_blank_number[2]);
                            break;
                        case "total_money":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[3]--;
                            log.info("???????????????" + right_shift_blank_number[3]);
                            break;
                        case "templatePaymentMethod":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[4]--;
                            log.info("???????????????" + right_shift_blank_number[4]);
                            break;
                        case "templateDiscount":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[5]--;
                            log.info("???????????????" + right_shift_blank_number[5]);
                            break;
                        case "templatePayable":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[6]--;
                            log.info("???????????????" + right_shift_blank_number[6]);
                            break;
                        case "templatePaymentMethod1":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[7]--;
                            log.info("???????????????" + right_shift_blank_number[7]);
                            break;
//                            case "templatePaymentMethod"2:
//                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
//                                right_shift_blank_number[8]--;
//                                log.info("???????????????" + right_shift_blank_number[8]);
//                                break;
                        case "templatePaymentMethod3":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[8]--;
                            log.info("???????????????" + right_shift_blank_number[8]);
                            break;
                        case "templateFooter1":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[9]--;
                            log.info("???????????????" + right_shift_blank_number[9]);
                            break;
                        case "templateFooter2":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[10]--;
                            log.info("???????????????" + right_shift_blank_number[10]);
                            break;
                        case "templateFooter3":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[11]--;
                            log.info("???????????????" + right_shift_blank_number[11]);
                            break;
                        case "templateFooter4":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[12]--;
                            log.info("???????????????" + right_shift_blank_number[12]);
                            break;
                        case "templateFooter5":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[13]--;
                            log.info("???????????????" + right_shift_blank_number[13]);
                            break;
                        case "templateFooter6":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[14]--;
                            log.info("???????????????" + right_shift_blank_number[14]);
                            break;
                        case "templateFooter7":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[15]--;
                            log.info("???????????????" + right_shift_blank_number[15]);
                            break;
                        case "templateFooter8":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[16]--;
                            log.info("???????????????" + right_shift_blank_number[16]);
                            break;
                        case "templateFooter9":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[17]--;
                            log.info("???????????????" + right_shift_blank_number[17]);
                            break;
                        case "templateFooter10":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[18]--;
                            log.info("???????????????" + right_shift_blank_number[18]);
                            break;
                        case "templateTicketBottom":
                            text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                            right_shift_blank_number[19]--;
                            log.info("???????????????" + right_shift_blank_number[19]);
                            break;
                    }

                }
            }
        });
        viewController.right_shift.setOnMouseClicked(event -> {
            if (text.getAlignment() == Pos.CENTER || text.getAlignment() == Pos.TOP_LEFT) {//????????????????????????
                switch (text.getId()) {
                    case "templateHeader":
                        right_shift_blank_number[0]++;
                        log.info("???????????????" + right_shift_blank_number[0]);
                        moveToRight(text);
                        break;
                    case "templateDocumentNumber":
                        right_shift_blank_number[1]++;
                        log.info("???????????????" + right_shift_blank_number[1]);
                        moveToRight(text);
                        break;
                    case "templateDate":
                        right_shift_blank_number[2]++;
                        log.info("???????????????" + right_shift_blank_number[2]);
                        moveToRight(text);
                        break;
                    case "total_money":
                        right_shift_blank_number[3]++;
                        log.info("???????????????" + right_shift_blank_number[3]);
                        moveToRight(text);
                        break;
                    case "templatePaymentMethod":
                        right_shift_blank_number[4]++;
                        log.info("???????????????" + right_shift_blank_number[4]);
                        moveToRight(text);
                        break;
                    case "templateDiscount":
                        right_shift_blank_number[5]++;
                        log.info("???????????????" + right_shift_blank_number[5]);
                        moveToRight(text);
                        break;
                    case "templatePayable":
                        right_shift_blank_number[6]++;
                        log.info("???????????????" + right_shift_blank_number[6]);
                        moveToRight(text);
                        break;
                    case "templatePaymentMethod1":
                        right_shift_blank_number[7]++;
                        log.info("???????????????" + right_shift_blank_number[7]);
                        moveToRight(text);
                        break;
//                        case "templatePaymentMethod"2:
//                            right_shift_blank_number[8]++;
//                            log.info("???????????????" + right_shift_blank_number[8]);
//                            text.setText(" " + text.getText().toString());
//                            break;
                    case "templatePaymentMethod3":
                        right_shift_blank_number[8]++;
                        log.info("???????????????" + right_shift_blank_number[8]);
                        moveToRight(text);
                        break;
                    case "templateFooter1":
                        right_shift_blank_number[9]++;
                        log.info("???????????????" + right_shift_blank_number[9]);
                        moveToRight(text);
                        break;
                    case "templateFooter2":
                        right_shift_blank_number[10]++;
                        log.info("???????????????" + right_shift_blank_number[10]);
                        moveToRight(text);
                        break;
                    case "templateFooter3":
                        right_shift_blank_number[11]++;
                        log.info("???????????????" + right_shift_blank_number[11]);
                        moveToRight(text);
                        break;
                    case "templateFooter4":
                        right_shift_blank_number[12]++;
                        log.info("???????????????" + right_shift_blank_number[12]);
                        moveToRight(text);
                        break;
                    case "templateFooter5":
                        right_shift_blank_number[13]++;
                        log.info("???????????????" + right_shift_blank_number[13]);
                        moveToRight(text);
                        break;
                    case "templateFooter6":
                        right_shift_blank_number[14]++;
                        log.info("???????????????" + right_shift_blank_number[14]);
                        moveToRight(text);
                        break;
                    case "templateFooter7":
                        right_shift_blank_number[15]++;
                        log.info("???????????????" + right_shift_blank_number[15]);
                        moveToRight(text);
                        break;
                    case "templateFooter8":
                        right_shift_blank_number[16]++;
                        log.info("???????????????" + right_shift_blank_number[16]);
                        moveToRight(text);
                        break;
                    case "templateFooter9":
                        right_shift_blank_number[17]++;
                        log.info("???????????????" + right_shift_blank_number[17]);
                        moveToRight(text);
                        break;
                    case "templateFooter10":
                        right_shift_blank_number[18]++;
                        log.info("???????????????" + right_shift_blank_number[18]);
                        moveToRight(text);
                        break;
                    case "templateTicketBottom":
                        right_shift_blank_number[19]++;
                        log.info("???????????????" + right_shift_blank_number[19]);
                        moveToRight(text);
                        break;
                }
            } else if (text.getAlignment() == Pos.TOP_RIGHT) {//???????????????
                if (text.getText().toString().indexOf(" ") == -1) {//?????????????????????????????????????????????text??????????????????????????????text???????????????????????????????????????
                } else {//??????!=-1??????????????????text???????????????text?????????????????????????????????????????????????????????text???????????????????????????
                    switch (text.getId()) {
                        case "templateHeader":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[0]--;
                            log.info("???????????????" + left_shift_blank_number[0]);
                            break;
                        case "templateDocumentNumber":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[1]--;
                            log.info("???????????????" + left_shift_blank_number[1]);
                            break;
                        case "templateDate":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[2]--;
                            log.info("???????????????" + left_shift_blank_number[2]);
                            break;
                        case "total_money":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[3]--;
                            log.info("???????????????" + left_shift_blank_number[3]);
                            break;
                        case "templatePaymentMethod":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[4]--;
                            log.info("???????????????" + left_shift_blank_number[4]);
                            break;
                        case "templateDiscount":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[5]--;
                            log.info("???????????????" + left_shift_blank_number[5]);
                            break;
                        case "templatePayable":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[6]--;
                            log.info("???????????????" + left_shift_blank_number[6]);
                            break;
                        case "templatePaymentMethod1":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[7]--;
                            log.info("???????????????" + left_shift_blank_number[7]);
                            break;
//                            case "templatePaymentMethod"2:
//                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
//                                left_shift_blank_number[8]--;
//                                log.info("???????????????" + left_shift_blank_number[8]);
//                                break;
                        case "templatePaymentMethod3":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[8]--;
                            log.info("???????????????" + left_shift_blank_number[8]);
                            break;
                        case "templateFooter1":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[9]--;
                            log.info("???????????????" + left_shift_blank_number[9]);
                            break;
                        case "templateFooter2":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[10]--;
                            log.info("???????????????" + left_shift_blank_number[10]);
                            break;
                        case "templateFooter3":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[11]--;
                            log.info("???????????????" + left_shift_blank_number[11]);
                            break;
                        case "templateFooter4":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[12]--;
                            log.info("???????????????" + left_shift_blank_number[12]);
                            break;
                        case "templateFooter5":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[13]--;
                            log.info("???????????????" + left_shift_blank_number[13]);
                            break;
                        case "templateFooter6":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[14]--;
                            log.info("???????????????" + left_shift_blank_number[14]);
                            break;
                        case "templateFooter7":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[15]--;
                            log.info("???????????????" + left_shift_blank_number[15]);
                            break;
                        case "templateFooter8":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[16]--;
                            log.info("???????????????" + left_shift_blank_number[16]);
                            break;
                        case "templateFooter9":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[17]--;
                            log.info("???????????????" + left_shift_blank_number[17]);
                            break;
                        case "templateFooter10":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[18]--;
                            log.info("???????????????" + left_shift_blank_number[18]);
                            break;
                        case "templateTicketBottom":
                            text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                            left_shift_blank_number[19]--;
                            log.info("???????????????" + left_shift_blank_number[19]);
                            break;
                    }
                }
            }
        });
    }

    private void moveToLeft(Label text) {
        log.error("L???TextView????????????????????????" + text.getText() + "????????????" + text.getText().length());
        String str = text.getText().toString().trim();
        if (text.getText().toString().indexOf(str) != -1) {
            text.setText(text.getText().toString() + " ");
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????xxxxx--------------------------???-?????????????????????????????????????????????????????????
            if (text.getText().toString().length() == MAX_Length_EditText && text.getText().toString().indexOf(str) == 0) {
                text.setAlignment(Pos.TOP_LEFT);
                text.setText(text.getText().trim());
//                viewController.controlKeepLeft.setImageResource(R.drawable.left_align_click);
//                viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//                viewController.controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            }
        }
    }

    private void moveToRight(Label text) {
        log.error("R???TextView????????????????????????" + text.getText());
        String str = text.getText().trim();
        if (text.getText().toString().indexOf(str) != -1 && text.getText().indexOf(str) != 30 - str.length()) {
            text.setText(" " + text.getText());
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????-----------------------xxxxx???-?????????????????????????????????????????????????????????
            if (text.getText().indexOf(str) == MAX_Length_EditText - str.length()) {
                text.setAlignment(Pos.TOP_RIGHT);
                text.setText(text.getText().trim());
//                viewController.controlKeepLeft.setImageResource(R.drawable.left_align_click);
//                viewController.controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
//                viewController.controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            }
        }
    }

    private void setTextBackgroudColor() {
        Background background = new Background(new BackgroundFill(Color.WHITE, null, null));

        viewController.templateHeader.setBackground(background);
        viewController.templateHeader.setTextFill(Color.BLACK);
        viewController.templateDocumentNumber.setBackground(background);
        viewController.templateDocumentNumber.setTextFill(Color.BLACK);
        viewController.templateDate.setBackground(background);
        viewController.templateDate.setTextFill(Color.BLACK);
        viewController.templateGoodsName.setBackground(background);
        viewController.templateGoodsNumber.setBackground(background);
        viewController.templateSubtotal.setBackground(background);
//        viewController.templateGoodsNameLayout.setBackground(background);
//        viewController.templateGoodsNumberLayout.setBackground(background);
//        viewController.templateSubtotalLayout.setBackground(background);
        viewController.templateTotalMoney.setBackground(background);
        viewController.templateTotalMoney.setTextFill(Color.BLACK);
        viewController.templatePaymentMethod.setBackground(background);
        viewController.templatePaymentMethod.setTextFill(Color.BLACK);
        viewController.templateDiscount.setBackground(background);
        viewController.templateDiscount.setTextFill(Color.BLACK);
        viewController.templatePayable.setBackground(background);
        viewController.templatePayable.setTextFill(Color.BLACK);
        viewController.templatePaymentMethod1.setBackground(background);
        viewController.templatePaymentMethod1.setTextFill(Color.BLACK);
//        "templatePaymentMethod"2.setBackground(background);
        viewController.templatePaymentMethod3.setBackground(background);
        viewController.templatePaymentMethod3.setTextFill(Color.BLACK);
        viewController.templateFooter1.setBackground(background);
        viewController.templateFooter1.setTextFill(Color.BLACK);
        viewController.templateFooter2.setBackground(background);
        viewController.templateFooter2.setTextFill(Color.BLACK);
        viewController.templateFooter3.setBackground(background);
        viewController.templateFooter3.setTextFill(Color.BLACK);
        viewController.templateFooter4.setBackground(background);
        viewController.templateFooter4.setTextFill(Color.BLACK);
        viewController.templateFooter5.setBackground(background);
        viewController.templateFooter5.setTextFill(Color.BLACK);
        viewController.templateFooter6.setBackground(background);
        viewController.templateFooter6.setTextFill(Color.BLACK);
        viewController.templateFooter7.setBackground(background);
        viewController.templateFooter7.setTextFill(Color.BLACK);
        viewController.templateFooter8.setBackground(background);
        viewController.templateFooter8.setTextFill(Color.BLACK);
        viewController.templateFooter9.setBackground(background);
        viewController.templateFooter9.setTextFill(Color.BLACK);
        viewController.templateFooter10.setBackground(background);
        viewController.templateFooter10.setTextFill(Color.BLACK);
//        viewController.templateLogo.setBackground(background);
        viewController.templateTicketBottom.setBackground(background);
        viewController.templateTicketBottom.setTextFill(Color.BLACK);
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param textView
     */
    private void setBoldButtonColor(Label textView) {
        // TODO
        if ("Bold".equals(textView.getFont().getStyle())) {
//            controlBold.setBackgroundColor(Color.YELLOW);
            viewController.controlBold.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            viewController.controlBold.setTextFill(Color.BLUE);
        } else {
//            controlBold.setBackground(background);
            viewController.controlBold.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            viewController.controlBold.setTextFill(Color.BLACK);
        }
    }

    private void initSpinner(final int smallSheetFrameID) {
        /**
         * ?????????SQLite????????????SyncDatetime??????1970????????????????????????
         * ????????????????????????????????????????????????List??????(??????i)
         * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * ???List?????????Adapter
         */
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("where F_SyncType != '%s'");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        log.info("Frame?????????????????????" + frameList.size());
        smallSheetFrame_IDList = new ArrayList<>();
        int a = 0;
        for (int i = 0; i < frameList.size(); i++) {
            smallSheetFrame_IDList.add(frameList.get(i).getID());//?????????????????????ID
        }

//        Collections.sort(smallSheetFrame_IDList);//??????ID????????????
        Collections.reverse(smallSheetFrame_IDList);//??????ID????????????

        String showSmallSheetNumber = "";
        String[] versionArray = new String[smallSheetFrame_IDList.size()];
        final Map<Integer, Integer> position = new HashMap<Integer, Integer>();
        for (int i = smallSheetFrame_IDList.size(); i > 0; i--) {
            versionArray[smallSheetFrame_IDList.size() - i] = "?????? " + i;
            position.put(Integer.valueOf(String.valueOf(smallSheetFrame_IDList.get(i - 1))), a++);
        }

        viewController.printFormatVersion.getItems().removeAll();
        viewController.printFormatVersion.getItems().addAll(versionArray);
        viewController.printFormatVersion.setItems(FXCollections.observableArrayList(versionArray));
        if (smallSheetFrameID == BaseSQLiteBO.INVALID_INT_ID || !position.containsKey(smallSheetFrameID)) { // ????????????SQLite??????????????????-1??????????????????????????? || ???????????????????????????????????????????????????????????????????????????
            viewController.printFormatVersion.setValue(showSmallSheetNumber);
            SelectPosition = 0;
        } else {
            showSmallSheetNumber = "?????? " + (position.get(smallSheetFrameID) + 1); // ??????????????????1????????????????????????+1
            viewController.printFormatVersion.setValue(showSmallSheetNumber); // ??????????????????????????????item
            SelectPosition = position.get(smallSheetFrameID);   //????????????position????????????
            selectedFrameID = smallSheetFrameID; // ?????????????????????????????????????????????ID?????????selectedFrameID??????????????????????????????CRUD
        }
    }

    /**
     * ??????SmallSheetActivity.java????????????SQLite??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private void initSmallSheetData(int smallSheetFrameID) {
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("where F_SyncType != '%s'");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        if (frameList != null) {
            if (frameList.size() > 0) {
                initSpinner(smallSheetFrameID);
            } else {
                initRightContent();
            }
        }
    }

    /**
     * ???UI?????????????????????????????????
     *
     * @param position ??????Spinner????????????item???position
     */
    private void showSelectedSmallSheet(int position) {
        selectedFrameID = smallSheetFrame_IDList.get(position);
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(selectedFrameID);
        //???????????????SmallSheetFrame
        smallSheetFrame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        //???????????????SmallSheetText
        smallSheetFrame.setSql(" where F_FrameID = '%s'");
        smallSheetFrame.setConditions(new String[]{String.valueOf(smallSheetFrame.getID())});
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, smallSheetFrame);
        System.out.println("====smallSheetTextList:" + smallSheetTextList);
        if (smallSheetTextList.size() != 20) {
            //??????  ????????????  ?????????????????????
//            AlertDialog dialog = new AlertDialog.Builder(getActivity())
//                    .setMessage("???????????????????????????")
//                    .setTitle("??????")
//                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            viewController.smallsheet_delete.performClick();
//                        }
//                    })
//                    .show();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("???????????????");
            alert.setHeaderText("????????????");
            alert.setContentText("???????????????????????????");
            alert.showAndWait();
        } else {
            // ???????????????TextView???Text??????????????????????????????????????????
            initTemplateSmallSheet(smallSheetTextList);
            showSelectedSmallSheetInUI(smallSheetFrame, smallSheetTextList);
        }
    }

    private File decodeUriAsBitmap(String url) {
        File file = null;
        try {
            log.info("decodeUriAsBitmap");
            file = new File(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

//    private int getBitmapMemory(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ???????????????????????????100????????????????????????????????????????????????baos???
//        return baos.toByteArray().length / 1024;
//    }
//
//    public static Bitmap compressImage(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ???????????????????????????100????????????????????????????????????????????????baos???
//        int options = 90;
//        while (baos.toByteArray().length / 1024 > 20) { // ?????????????????????????????????????????????100kb,??????????????????
//            baos.reset(); // ??????baos?????????baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ????????????options%?????????????????????????????????baos???
//            options -= 5;// ???????????????10
//            log.info("?????????" + baos.toByteArray().length / 1024);
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ?????????????????????baos?????????ByteArrayInputStream???
//        Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);// ???ByteArrayInputStream??????????????????
//        return bitmap;
//    }

    /**
     * ???UI?????????smallSheetText??????
     *
     * @return
     */
    protected List<SmallSheetText> getSmallSheetTextFromView() {
        smallSheetTextLists.clear();

        setSmallSheetText(viewController.templateHeader);
        setSmallSheetText(viewController.templateDocumentNumber);
        setSmallSheetText(viewController.templateDate);
        setSmallSheetText(viewController.templateTotalMoney);
        setSmallSheetText(viewController.templatePaymentMethod);
        setSmallSheetText(viewController.templateDiscount);
        setSmallSheetText(viewController.templatePayable);
        setSmallSheetText(viewController.templatePaymentMethod1);
//        setSmallSheetText("templatePaymentMethod"2);
        setSmallSheetText(viewController.templatePaymentMethod3);
        setSmallSheetText(viewController.templateFooter1);
        setSmallSheetText(viewController.templateFooter2);
        setSmallSheetText(viewController.templateFooter3);
        setSmallSheetText(viewController.templateFooter4);
        setSmallSheetText(viewController.templateFooter5);
        setSmallSheetText(viewController.templateFooter6);
        setSmallSheetText(viewController.templateFooter7);
        setSmallSheetText(viewController.templateFooter8);
        setSmallSheetText(viewController.templateFooter9);
        setSmallSheetText(viewController.templateFooter10);
        setSmallSheetText(viewController.templateTicketBottom);

        return smallSheetTextLists;
    }

    private SmallSheetText setSmallSheetText(Label textView) {
        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setContent(textView.getText());
        smallSheetText.setSize((float) textView.getFont().getSize());
        if ("Bold".equals(textView.getFont().getStyle())) {
            smallSheetText.setBold(1);
        } else {
            smallSheetText.setBold(0);
        }
        if (textView.getAlignment() == Pos.TOP_RIGHT) {
            smallSheetText.setGravity(GRAVITY_RIGHT);
        } else if (textView.getAlignment() == Pos.CENTER) {
            smallSheetText.setGravity(GRAVITY_CENTRE);
        } else if (textView.getAlignment() == Pos.TOP_LEFT) {
            smallSheetText.setGravity(GRAVITY_LEFT);
        }

        smallSheetTextLists.add(smallSheetText);
        return smallSheetText;
    }

    private void delete_footer(Label textView, TextField editText, BorderPane layout) {
        textView.setVisible(false);
        textView.setManaged(false);
        textView.setText(defaultText);
        editText.setText(defaultText);
        layout.setVisible(false);
        layout.setManaged(false);
    }

    public void onItemSelected() {
        viewController.printFormatVersion.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            showSelectedSmallSheet(newValue.intValue());
            setAddFooterView(); // ???????????????????????????????????????????????????????????????+?????????????????????????????????
            SelectPosition = newValue.intValue();
            System.out.println("????????????" + selectedFrameID);
        });
    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1: // ??????
                initSpinner(Integer.valueOf(String.valueOf(selectedFrameID)));
                if ((GlobalController.getInstance().getSessionID() == null || !NetworkUtils.isNetworkAvalible())) {
                    ToastUtil.toast("???????????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                } else {
                    ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                }
                break;
            case 5: // ????????????????????????
                initSpinner(currentSmallSheetID);
                ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                break;
            case 2: // ????????????????????????(????????????????????????)
                initSpinner(currentSmallSheetID);
                initSmallSheetData(currentSmallSheetID);
                viewController.smallsheet_delete.setDisable(false);
                viewController.closeLoadingDialog();
                break;
            case 3: // ?????????????????????????????????
                viewController.smallsheet_delete.setDisable(false);
                initSmallSheetData(selectedFrameID); // ????????????????????????
                viewController.closeLoadingDialog();
                ToastUtil.toast(msg.obj.toString(), ToastUtil.LENGTH_SHORT);
                break;
            case 4:
                // ??????????????????????????????????????????
//                initSpinner(Integer.valueOf(String.valueOf(selectedFrameID)));
                initSmallSheetData(Integer.valueOf(String.valueOf(selectedFrameID)));
                viewController.smallsheet_delete.setDisable(false);
                if ((GlobalController.getInstance().getSessionID() == null || !NetworkUtils.isNetworkAvalible())) {
                    ToastUtil.toast("???????????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                } else {
                    ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                }
                break;
            case 6: // ????????????????????????(???????????????)
                initSpinner(currentSmallSheetID);
                initSmallSheetData(currentSmallSheetID);
                viewController.smallsheet_delete.setDisable(false);
                viewController.closeLoadingDialog();
                if ((GlobalController.getInstance().getSessionID() == null || !NetworkUtils.isNetworkAvalible())) {
                    ToastUtil.toast("???????????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                } else {
                    ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                }
                break;
        }
    }

    /**
     * ???????????????emoji??????
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //??????????????????,???????????????Emoji??????
                return true;
            }
        }
        return false;
    }

    /**
     * ???????????????Emoji
     *
     * @param codePoint ?????????????????????
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    // ???????????????????????????getBytes("gbk")???getBytes("utf-8")??????????????????1???1
    // ???????????????????????????getBytes("gbk")???getBytes("utf-8")??????????????????1???1?????????3???2
    // ??????getBytes("gbk")???getBytes("utf-8")??????????????????????????????????????????????????????????????????
    // isEmojiCharacter()???????????????????????????????????????????????????????????????,??????getBytes("utf-8")????????????6
    // ??????getBytes("gbk")????????????2????????????
    // ??????????????????????????????????????????????????????????????????
    public static boolean isSpecialEmoji(CharSequence input) {
        try {
            return (input.toString().getBytes("utf-8").length == 6 && input.toString().getBytes("gbk").length == 2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    // TODO
    public void onDetach() {
//        currentSmallSheetID = BaseSQLiteBO.INVALID_INT_ID;
//        selectedFrameID = null;
//          smallSheetTextPresenter = null;
//        smallSheetFramePresenter = null;
//        SelectPosition = 0;
//        spinner_Adapter = null;
    }

    public void setAddFooterView() {
        if (viewController.designFooter2_layout.isVisible() && viewController.designFooter2_layout.isVisible()
                && viewController.designFooter3Layout.isVisible() && viewController.designFooter4Layout.isVisible()
                && viewController.designFooter4Layout.isVisible() && viewController.designFooter5Layout.isVisible()
                && viewController.designFooter6Layout.isVisible() && viewController.designFooter7Layout.isVisible()
                && viewController.designFooter8Layout.isVisible() && viewController.designFooter9Layout.isVisible()
                && viewController.designFooter9Layout.isVisible() && viewController.designFooter10Layout.isVisible()
        ) {
            viewController.add_footer.setVisible(false);
            viewController.add_footer.setManaged(false);
        } else {
            viewController.add_footer.setVisible(true);
            viewController.add_footer.setManaged(true);
        }
    }

    @Override
    public void selectItem(String radiobutton) {

    }
}
