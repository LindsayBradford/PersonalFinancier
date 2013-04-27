package blacksmyth.general;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.AbstractButton;
import javax.swing.JTabbedPane;

/**
 * A bridge/singleton pattern implementation providing convenient access to the 
 * "Font Awesome" font set (rev 3.0.2). The glyphs from this font set are used as an 
 * alternative to icon images in Swing components. 
 * @author linds
 */
public final class FontIconProvider {
  private static final String FONT_FILENAME = "fontawesome-webfont.ttf";
  private static Font BASE_ICON_FONT;
  private static Font MAIN_ICON_FONT;
  
  private static FontIconProvider instance;

  protected FontIconProvider() {
    
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    BASE_ICON_FONT = ResourceBridge.getFont(FONT_FILENAME);
    ge.registerFont(BASE_ICON_FONT);
    MAIN_ICON_FONT = BASE_ICON_FONT.deriveFont(Font.PLAIN, 24);
  }
  
  /**
   * Provides the single instance of this class, configured to provide 
   * icon glyphs from a dedicated font, accessible only via this instance.
   * @return
   */
  public static FontIconProvider getInstance() {
    if (instance == null) {
      instance = new FontIconProvider();
    }
    return instance;
  }

  /**
   * Configures the supplied <tt>button</tt> to display the provided
   * <tt>iconAsChar</tt> as button icon by using the specialised 
   * font within this class for glyph production.  The character
   * specified should only bee one of the characters supplied via
   * this class.
   * @param button
   * @param iconAsChar
   */
  public void setGlyphAsText(AbstractButton button, char iconAsChar) {
    button.setFont(MAIN_ICON_FONT);
    button.setText(String.valueOf(iconAsChar));
  }

  public void setGlyphAsTitle(JTabbedPane pane, int tabIndex, char iconAsChar) {
    pane.setFont(MAIN_ICON_FONT);
    pane.setTitleAt(tabIndex, String.valueOf(iconAsChar));
  }

  public static final char icon_glass                = '\uf000'; 
  public static final char icon_music                = '\uf001'; 
  public static final char icon_search               = '\uf002'; 
  public static final char icon_envelope             = '\uf003'; 
  public static final char icon_heart                = '\uf004'; 
  public static final char icon_star                 = '\uf005'; 
  public static final char icon_star_empty           = '\uf006'; 
  public static final char icon_user                 = '\uf007'; 
  public static final char icon_film                 = '\uf008'; 
  public static final char icon_th_large             = '\uf009'; 
  public static final char icon_th                   = '\uf00a'; 
  public static final char icon_th_list              = '\uf00b'; 
  public static final char icon_ok                   = '\uf01c'; 
  public static final char icon_remove               = '\uf00d'; 
  public static final char icon_zoom_in              = '\uf00e'; 
  public static final char icon_zoom_out             = '\uf010'; 
  public static final char icon_off                  = '\uf011'; 
  public static final char icon_signal               = '\uf012'; 
  public static final char icon_cog                  = '\uf013'; 
  public static final char icon_trash                = '\uf014'; 
  public static final char icon_home                 = '\uf015'; 
  public static final char icon_file                 = '\uf016'; 
  public static final char icon_time                 = '\uf017'; 
  public static final char icon_road                 = '\uf018'; 
  public static final char icon_download_alt         = '\uf019'; 
  public static final char icon_download             = '\uf01a'; 
  public static final char icon_upload               = '\uf01b'; 
  public static final char icon_inbox                = '\uf01c'; 
  public static final char icon_play_circle          = '\uf01d'; 
  public static final char icon_repeat               = '\uf01e'; 
  public static final char icon_refresh              = '\uf021'; 
  public static final char icon_list_alt             = '\uf022'; 
  public static final char icon_lock                 = '\uf023'; 
  public static final char icon_flag                 = '\uf024'; 
  public static final char icon_headphones           = '\uf025'; 
  public static final char icon_volume_off           = '\uf026'; 
  public static final char icon_volume_down          = '\uf027'; 
  public static final char icon_volume_up            = '\uf028'; 
  public static final char icon_qrcode               = '\uf029'; 
  public static final char icon_barcode              = '\uf02a'; 
  public static final char icon_tag                  = '\uf02b'; 
  public static final char icon_tags                 = '\uf02c'; 
  public static final char icon_book                 = '\uf02d'; 
  public static final char icon_bookmark             = '\uf02e'; 
  public static final char icon_print                = '\uf02f'; 
  public static final char icon_camera               = '\uf030'; 
  public static final char icon_font                 = '\uf031'; 
  public static final char icon_bold                 = '\uf032'; 
  public static final char icon_italic               = '\uf033'; 
  public static final char icon_text_height          = '\uf034'; 
  public static final char icon_text_width           = '\uf035'; 
  public static final char icon_align_left           = '\uf036'; 
  public static final char icon_align_center         = '\uf037'; 
  public static final char icon_align_right          = '\uf038'; 
  public static final char icon_align_justify        = '\uf039'; 
  public static final char icon_list                 = '\uf03a'; 
  public static final char icon_indent_left          = '\uf03b'; 
  public static final char icon_indent_right         = '\uf03c'; 
  public static final char icon_facetime_video       = '\uf03d'; 
  public static final char icon_picture              = '\uf03e'; 
  public static final char icon_pencil               = '\uf040'; 
  public static final char icon_map_marker           = '\uf041'; 
  public static final char icon_adjust               = '\uf042'; 
  public static final char icon_tint                 = '\uf043'; 
  public static final char icon_edit                 = '\uf044'; 
  public static final char icon_share                = '\uf045'; 
  public static final char icon_check                = '\uf046'; 
  public static final char icon_move                 = '\uf047'; 
  public static final char icon_step_backward        = '\uf048'; 
  public static final char icon_fast_backward        = '\uf049'; 
  public static final char icon_backward             = '\uf04a'; 
  public static final char icon_play                 = '\uf04b'; 
  public static final char icon_pause                = '\uf04c'; 
  public static final char icon_stop                 = '\uf04d'; 
  public static final char icon_forward              = '\uf04e'; 
  public static final char icon_fast_forward         = '\uf050'; 
  public static final char icon_step_forward         = '\uf051'; 
  public static final char icon_eject                = '\uf052'; 
  public static final char icon_chevron_left         = '\uf053'; 
  public static final char icon_chevron_right        = '\uf054'; 
  public static final char icon_plus_sign            = '\uf055'; 
  public static final char icon_minus_sign           = '\uf056'; 
  public static final char icon_remove_sign          = '\uf057'; 
  public static final char icon_ok_sign              = '\uf058'; 
  public static final char icon_question_sign        = '\uf059'; 
  public static final char icon_info_sign            = '\uf05a'; 
  public static final char icon_screenshot           = '\uf05b'; 
  public static final char icon_remove_circle        = '\uf05c'; 
  public static final char icon_ok_circle            = '\uf05d'; 
  public static final char icon_ban_circle           = '\uf05e'; 
  public static final char icon_arrow_left           = '\uf060'; 
  public static final char icon_arrow_right          = '\uf061'; 
  public static final char icon_arrow_up             = '\uf062'; 
  public static final char icon_arrow_down           = '\uf063'; 
  public static final char icon_share_alt            = '\uf064'; 
  public static final char icon_resize_full          = '\uf065'; 
  public static final char icon_resize_small         = '\uf066'; 
  public static final char icon_plus                 = '\uf067'; 
  public static final char icon_minus                = '\uf068'; 
  public static final char icon_asterisk             = '\uf069'; 
  public static final char icon_exclamation_sign     = '\uf06a'; 
  public static final char icon_gift                 = '\uf06b'; 
  public static final char icon_leaf                 = '\uf06c'; 
  public static final char icon_fire                 = '\uf06d'; 
  public static final char icon_eye_open             = '\uf06e'; 
  public static final char icon_eye_close            = '\uf070'; 
  public static final char icon_warning_sign         = '\uf071'; 
  public static final char icon_plane                = '\uf072'; 
  public static final char icon_calendar             = '\uf073'; 
  public static final char icon_random               = '\uf074'; 
  public static final char icon_comment              = '\uf075'; 
  public static final char icon_magnet               = '\uf076'; 
  public static final char icon_chevron_up           = '\uf077'; 
  public static final char icon_chevron_down         = '\uf078'; 
  public static final char icon_retweet              = '\uf079'; 
  public static final char icon_shopping_cart        = '\uf07a'; 
  public static final char icon_folder_close         = '\uf07b'; 
  public static final char icon_folder_open          = '\uf07c'; 
  public static final char icon_resize_vertical      = '\uf07d'; 
  public static final char icon_resize_horizontal    = '\uf07e'; 
  public static final char icon_bar_chart            = '\uf080'; 
  public static final char icon_twitter_sign         = '\uf081'; 
  public static final char icon_facebook_sign        = '\uf082'; 
  public static final char icon_camera_retro         = '\uf083'; 
  public static final char icon_key                  = '\uf084'; 
  public static final char icon_cogs                 = '\uf085'; 
  public static final char icon_comments             = '\uf086'; 
  public static final char icon_thumbs_up            = '\uf087'; 
  public static final char icon_thumbs_down          = '\uf088'; 
  public static final char icon_star_half            = '\uf089'; 
  public static final char icon_heart_empty          = '\uf08a'; 
  public static final char icon_signout              = '\uf08b'; 
  public static final char icon_linkedin_sign        = '\uf08c'; 
  public static final char icon_pushpin              = '\uf08d'; 
  public static final char icon_external_link        = '\uf08e'; 
  public static final char icon_signin               = '\uf090'; 
  public static final char icon_trophy               = '\uf091'; 
  public static final char icon_github_sign          = '\uf092'; 
  public static final char icon_upload_alt           = '\uf093'; 
  public static final char icon_lemon                = '\uf094'; 
  public static final char icon_phone                = '\uf095'; 
  public static final char icon_check_empty          = '\uf096'; 
  public static final char icon_bookmark_empty       = '\uf097'; 
  public static final char icon_phone_sign           = '\uf098'; 
  public static final char icon_twitter              = '\uf099'; 
  public static final char icon_facebook             = '\uf09a'; 
  public static final char icon_github               = '\uf09b'; 
  public static final char icon_unlock               = '\uf09c'; 
  public static final char icon_credit_card          = '\uf09d'; 
  public static final char icon_rss                  = '\uf09e'; 
  public static final char icon_hdd                  = '\uf0a0'; 
  public static final char icon_bullhorn             = '\uf0a1'; 
  public static final char icon_bell                 = '\uf0a2'; 
  public static final char icon_certificate          = '\uf0a3'; 
  public static final char icon_hand_right           = '\uf0a4'; 
  public static final char icon_hand_left            = '\uf0a5'; 
  public static final char icon_hand_up              = '\uf0a6'; 
  public static final char icon_hand_down            = '\uf0a7'; 
  public static final char icon_circle_arrow_left    = '\uf0a8'; 
  public static final char icon_circle_arrow_right   = '\uf0a9'; 
  public static final char icon_circle_arrow_up      = '\uf0aa'; 
  public static final char icon_circle_arrow_down    = '\uf0ab'; 
  public static final char icon_globe                = '\uf0ac'; 
  public static final char icon_wrench               = '\uf0ad'; 
  public static final char icon_tasks                = '\uf0ae'; 
  public static final char icon_filter               = '\uf0b0'; 
  public static final char icon_briefcase            = '\uf0b1'; 
  public static final char icon_fullscreen           = '\uf0b2'; 
  public static final char icon_group                = '\uf0c0'; 
  public static final char icon_link                 = '\uf0c1'; 
  public static final char icon_cloud                = '\uf0c2'; 
  public static final char icon_beaker               = '\uf0c3'; 
  public static final char icon_cut                  = '\uf0c4'; 
  public static final char icon_copy                 = '\uf0c5'; 
  public static final char icon_paper_clip           = '\uf0c6'; 
  public static final char icon_save                 = '\uf0c7'; 
  public static final char icon_sign_blank           = '\uf0c8'; 
  public static final char icon_reorder              = '\uf0c9'; 
  public static final char icon_list_ul              = '\uf0ca'; 
  public static final char icon_list_ol              = '\uf0cb'; 
  public static final char icon_strikethrough        = '\uf0cc'; 
  public static final char icon_underline            = '\uf0cd'; 
  public static final char icon_table                = '\uf0ce'; 
  public static final char icon_magic                = '\uf0d0'; 
  public static final char icon_truck                = '\uf0d1'; 
  public static final char icon_pinterest            = '\uf0d2'; 
  public static final char icon_pinterest_sign       = '\uf0d3'; 
  public static final char icon_google_plus_sign     = '\uf0d4'; 
  public static final char icon_google_plus          = '\uf0d5'; 
  public static final char icon_money                = '\uf0d6'; 
  public static final char icon_caret_down           = '\uf0d7'; 
  public static final char icon_caret_up             = '\uf0d8'; 
  public static final char icon_caret_left           = '\uf0d9'; 
  public static final char icon_caret_right          = '\uf0da'; 
  public static final char icon_columns              = '\uf0db'; 
  public static final char icon_sort                 = '\uf0dc'; 
  public static final char icon_sort_down            = '\uf0dd'; 
  public static final char icon_sort_up              = '\uf0de'; 
  public static final char icon_envelope_alt         = '\uf0e0'; 
  public static final char icon_linkedin             = '\uf0e1'; 
  public static final char icon_undo                 = '\uf0e2'; 
  public static final char icon_legal                = '\uf0e3'; 
  public static final char icon_dashboard            = '\uf0e4'; 
  public static final char icon_comment_alt          = '\uf0e5'; 
  public static final char icon_comments_alt         = '\uf0e6'; 
  public static final char icon_bolt                 = '\uf0e7'; 
  public static final char icon_sitemap              = '\uf0e8'; 
  public static final char icon_umbrella             = '\uf0e9'; 
  public static final char icon_paste                = '\uf0ea'; 
  public static final char icon_lightbulb            = '\uf0eb'; 
  public static final char icon_exchange             = '\uf0ec'; 
  public static final char icon_cloud_download       = '\uf0ed'; 
  public static final char icon_cloud_upload         = '\uf0ee'; 
  public static final char icon_user_md              = '\uf0f0'; 
  public static final char icon_stethoscope          = '\uf0f1'; 
  public static final char icon_suitcase             = '\uf0f2'; 
  public static final char icon_bell_alt             = '\uf0f3'; 
  public static final char icon_coffee               = '\uf0f4'; 
  public static final char icon_food                 = '\uf0f5'; 
  public static final char icon_file_alt             = '\uf0f6'; 
  public static final char icon_building             = '\uf0f7'; 
  public static final char icon_hospital             = '\uf0f8'; 
  public static final char icon_ambulance            = '\uf0f9'; 
  public static final char icon_medkit               = '\uf0fa'; 
  public static final char icon_fighter_jet          = '\uf0fb'; 
  public static final char icon_beer                 = '\uf0fc'; 
  public static final char icon_h_sign               = '\uf0fd'; 
  public static final char icon_plus_sign_alt        = '\uf0fe'; 
  public static final char icon_double_angle_left    = '\uf100'; 
  public static final char icon_double_angle_right   = '\uf101'; 
  public static final char icon_double_angle_up      = '\uf102'; 
  public static final char icon_double_angle_down    = '\uf103'; 
  public static final char icon_angle_left           = '\uf104'; 
  public static final char icon_angle_right          = '\uf105'; 
  public static final char icon_angle_up             = '\uf106'; 
  public static final char icon_angle_down           = '\uf107'; 
  public static final char icon_desktop              = '\uf108'; 
  public static final char icon_laptop               = '\uf109'; 
  public static final char icon_tablet               = '\uf10a'; 
  public static final char icon_mobile_phone         = '\uf10b'; 
  public static final char icon_circle_blank         = '\uf10c'; 
  public static final char icon_quote_left           = '\uf10d'; 
  public static final char icon_quote_right          = '\uf10e'; 
  public static final char icon_spinner              = '\uf110'; 
  public static final char icon_circle               = '\uf111'; 
  public static final char icon_reply                = '\uf112'; 
  public static final char icon_github_alt           = '\uf113'; 
  public static final char icon_folder_close_alt     = '\uf114'; 
  public static final char icon_folder_open_alt      = '\uf115'; 
}
