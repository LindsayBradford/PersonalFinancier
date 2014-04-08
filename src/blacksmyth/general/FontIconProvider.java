/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.AbstractButton;
import javax.swing.JTabbedPane;

/**
 * A bridge/singleton pattern implementation providing convenient access to the 
 * "Font Awesome" font set (rev 4.0.3). The glyphs from this font set are used as an 
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

  public static final char icon_glass  = '\uf000';
  public static final char icon_music  = '\uf001';
  public static final char icon_search  = '\uf002';
  public static final char icon_envelope_o  = '\uf003';
  public static final char icon_heart  = '\uf004';
  public static final char icon_star  = '\uf005';
  public static final char icon_star_o  = '\uf006';
  public static final char icon_user  = '\uf007';
  public static final char icon_film  = '\uf008';
  public static final char icon_th_large  = '\uf009';
  public static final char icon_th  = '\uf00a';
  public static final char icon_th_list  = '\uf00b';
  public static final char icon_check  = '\uf00c';
  public static final char icon_times  = '\uf00d';
  public static final char icon_search_plus  = '\uf00e';
  public static final char icon_search_minus  = '\uf010';
  public static final char icon_power_off  = '\uf011';
  public static final char icon_signal  = '\uf012';
  public static final char icon_gear = '\uf013';
  public static final char icon_cog = '\uf013';
  public static final char icon_trash_o  = '\uf014';
  public static final char icon_home  = '\uf015';
  public static final char icon_file_o  = '\uf016';
  public static final char icon_clock_o  = '\uf017';
  public static final char icon_road  = '\uf018';
  public static final char icon_download  = '\uf019';
  public static final char icon_arrow_circle_o_down  = '\uf01a';
  public static final char icon_arrow_circle_o_up  = '\uf01b';
  public static final char icon_inbox  = '\uf01c';
  public static final char icon_play_circle_o  = '\uf01d';
  public static final char icon_rotate_right = '\uf01e';
  public static final char icon_repeat = '\uf01e';
  public static final char icon_refresh  = '\uf021';
  public static final char icon_list_alt  = '\uf022';
  public static final char icon_lock  = '\uf023';
  public static final char icon_flag  = '\uf024';
  public static final char icon_headphones  = '\uf025';
  public static final char icon_volume_off  = '\uf026';
  public static final char icon_volume_down  = '\uf027';
  public static final char icon_volume_up  = '\uf028';
  public static final char icon_qrcode  = '\uf029';
  public static final char icon_barcode  = '\uf02a';
  public static final char icon_tag  = '\uf02b';
  public static final char icon_tags  = '\uf02c';
  public static final char icon_book  = '\uf02d';
  public static final char icon_bookmark  = '\uf02e';
  public static final char icon_print  = '\uf02f';
  public static final char icon_camera  = '\uf030';
  public static final char icon_font  = '\uf031';
  public static final char icon_bold  = '\uf032';
  public static final char icon_italic  = '\uf033';
  public static final char icon_text_height  = '\uf034';
  public static final char icon_text_width  = '\uf035';
  public static final char icon_align_left  = '\uf036';
  public static final char icon_align_center  = '\uf037';
  public static final char icon_align_right  = '\uf038';
  public static final char icon_align_justify  = '\uf039';
  public static final char icon_list  = '\uf03a';
  public static final char icon_dedent = '\uf03b';
  public static final char icon_outdent  = '\uf03b';
  public static final char icon_indent  = '\uf03c';
  public static final char icon_video_camera  = '\uf03d';
  public static final char icon_picture_o  = '\uf03e';
  public static final char icon_pencil  = '\uf040';
  public static final char icon_map_marker  = '\uf041';
  public static final char icon_adjust  = '\uf042';
  public static final char icon_tint  = '\uf043';
  public static final char icon_edit  = '\uf044';
  public static final char icon_pencil_square_o  = '\uf044';
  public static final char icon_share_square_o  = '\uf045';
  public static final char icon_check_square_o  = '\uf046';
  public static final char icon_arrows  = '\uf047';
  public static final char icon_step_backward  = '\uf048';
  public static final char icon_fast_backward  = '\uf049';
  public static final char icon_backward  = '\uf04a';
  public static final char icon_play  = '\uf04b';
  public static final char icon_pause  = '\uf04c';
  public static final char icon_stop  = '\uf04d';
  public static final char icon_forward  = '\uf04e';
  public static final char icon_fast_forward  = '\uf050';
  public static final char icon_step_forward  = '\uf051';
  public static final char icon_eject  = '\uf052';
  public static final char icon_chevron_left  = '\uf053';
  public static final char icon_chevron_right  = '\uf054';
  public static final char icon_plus_circle  = '\uf055';
  public static final char icon_minus_circle  = '\uf056';
  public static final char icon_times_circle  = '\uf057';
  public static final char icon_check_circle  = '\uf058';
  public static final char icon_question_circle  = '\uf059';
  public static final char icon_info_circle  = '\uf05a';
  public static final char icon_crosshairs  = '\uf05b';
  public static final char icon_times_circle_o  = '\uf05c';
  public static final char icon_check_circle_o  = '\uf05d';
  public static final char icon_ban  = '\uf05e';
  public static final char icon_arrow_left  = '\uf060';
  public static final char icon_arrow_right  = '\uf061';
  public static final char icon_arrow_up  = '\uf062';
  public static final char icon_arrow_down  = '\uf063';
  public static final char icon_mail_forward = '\uf064';
  public static final char icon_share  = '\uf064';
  public static final char icon_expand  = '\uf065';
  public static final char icon_compress  = '\uf066';
  public static final char icon_plus  = '\uf067';
  public static final char icon_minus  = '\uf068';
  public static final char icon_asterisk  = '\uf069';
  public static final char icon_exclamation_circle  = '\uf06a';
  public static final char icon_gift  = '\uf06b';
  public static final char icon_leaf  = '\uf06c';
  public static final char icon_fire  = '\uf06d';
  public static final char icon_eye  = '\uf06e';
  public static final char icon_eye_slash  = '\uf070';
  public static final char icon_warning = '\uf071';
  public static final char icon_exclamation_triangle  = '\uf071';
  public static final char icon_plane  = '\uf072';
  public static final char icon_calendar  = '\uf073';
  public static final char icon_random  = '\uf074';
  public static final char icon_comment  = '\uf075';
  public static final char icon_magnet  = '\uf076';
  public static final char icon_chevron_up  = '\uf077';
  public static final char icon_chevron_down  = '\uf078';
  public static final char icon_retweet  = '\uf079';
  public static final char icon_shopping_cart  = '\uf07a';
  public static final char icon_folder  = '\uf07b';
  public static final char icon_folder_open  = '\uf07c';
  public static final char icon_arrows_v  = '\uf07d';
  public static final char icon_arrows_h  = '\uf07e';
  public static final char icon_bar_chart_o  = '\uf080';
  public static final char icon_twitter_square  = '\uf081';
  public static final char icon_facebook_square  = '\uf082';
  public static final char icon_camera_retro  = '\uf083';
  public static final char icon_key  = '\uf084';
  public static final char icon_gears = '\uf085';
  public static final char icon_cogs  = '\uf085';
  public static final char icon_comments  = '\uf086';
  public static final char icon_thumbs_o_up  = '\uf087';
  public static final char icon_thumbs_o_down  = '\uf088';
  public static final char icon_star_half  = '\uf089';
  public static final char icon_heart_o  = '\uf08a';
  public static final char icon_sign_out  = '\uf08b';
  public static final char icon_linkedin_square  = '\uf08c';
  public static final char icon_thumb_tack  = '\uf08d';
  public static final char icon_external_link  = '\uf08e';
  public static final char icon_sign_in  = '\uf090';
  public static final char icon_trophy  = '\uf091';
  public static final char icon_github_square  = '\uf092';
  public static final char icon_upload  = '\uf093';
  public static final char icon_lemon_o  = '\uf094';
  public static final char icon_phone  = '\uf095';
  public static final char icon_square_o  = '\uf096';
  public static final char icon_bookmark_o  = '\uf097';
  public static final char icon_phone_square  = '\uf098';
  public static final char icon_twitter  = '\uf099';
  public static final char icon_facebook  = '\uf09a';
  public static final char icon_github  = '\uf09b';
  public static final char icon_unlock  = '\uf09c';
  public static final char icon_credit_card  = '\uf09d';
  public static final char icon_rss  = '\uf09e';
  public static final char icon_hdd_o  = '\uf0a0';
  public static final char icon_bullhorn  = '\uf0a1';
  public static final char icon_bell  = '\uf0f3';
  public static final char icon_certificate  = '\uf0a3';
  public static final char icon_hand_o_right  = '\uf0a4';
  public static final char icon_hand_o_left  = '\uf0a5';
  public static final char icon_hand_o_up  = '\uf0a6';
  public static final char icon_hand_o_down  = '\uf0a7';
  public static final char icon_arrow_circle_left  = '\uf0a8';
  public static final char icon_arrow_circle_right  = '\uf0a9';
  public static final char icon_arrow_circle_up  = '\uf0aa';
  public static final char icon_arrow_circle_down  = '\uf0ab';
  public static final char icon_globe  = '\uf0ac';
  public static final char icon_wrench  = '\uf0ad';
  public static final char icon_tasks  = '\uf0ae';
  public static final char icon_filter  = '\uf0b0';
  public static final char icon_briefcase  = '\uf0b1';
  public static final char icon_arrows_alt  = '\uf0b2';
  public static final char icon_group = '\uf0c0';
  public static final char icon_users  = '\uf0c0';
  public static final char icon_chain = '\uf0c1';
  public static final char icon_link  = '\uf0c1';
  public static final char icon_cloud  = '\uf0c2';
  public static final char icon_flask  = '\uf0c3';
  public static final char icon_cut = '\uf0c4';
  public static final char icon_scissors  = '\uf0c4';
  public static final char icon_copy = '\uf0c5';
  public static final char icon_files_o  = '\uf0c5';
  public static final char icon_paperclip  = '\uf0c6'; 
  public static final char icon_save = '\uf0c7'; 
  public static final char icon_floppy_o  = '\uf0c7'; 
  public static final char icon_square  = '\uf0c8';
  public static final char icon_bars  = '\uf0c9';
  public static final char icon_list_ul  = '\uf0ca';
  public static final char icon_list_ol  = '\uf0cb';
  public static final char icon_strikethrough  = '\uf0cc';
  public static final char icon_underline  = '\uf0cd';
  public static final char icon_table  = '\uf0ce';
  public static final char icon_magic  = '\uf0d0';
  public static final char icon_truck  = '\uf0d1';
  public static final char icon_pinterest  = '\uf0d2';
  public static final char icon_pinterest_square  = '\uf0d3';
  public static final char icon_google_plus_square  = '\uf0d4';
  public static final char icon_google_plus  = '\uf0d5';
  public static final char icon_money  = '\uf0d6';
  public static final char icon_caret_down  = '\uf0d7';
  public static final char icon_caret_up  = '\uf0d8';
  public static final char icon_caret_left  = '\uf0d9';
  public static final char icon_caret_right  = '\uf0da';
  public static final char icon_columns  = '\uf0db';
  public static final char icon_unsorted = '\uf0dc';
  public static final char icon_sort  = '\uf0dc';
  public static final char icon_sort_down = '\uf0dd';
  public static final char icon_sort_asc  = '\uf0dd';
  public static final char icon_sort_up = '\uf0de';
  public static final char icon_sort_desc  = '\uf0de';
  public static final char icon_envelope  = '\uf0e0';
  public static final char icon_linkedin  = '\uf0e1';
  public static final char icon_rotate_left = '\uf0e2';
  public static final char icon_undo  = '\uf0e2';
  public static final char icon_legal = '\uf0e3';
  public static final char icon_gavel  = '\uf0e3';
  public static final char icon_dashboard = '\uf0e4';
  public static final char icon_tachometer  = '\uf0e4';
  public static final char icon_comment_o  = '\uf0e5';
  public static final char icon_comments_o  = '\uf0e6';
  public static final char icon_flash = '\uf0e7';
  public static final char icon_bolt  = '\uf0e7';
  public static final char icon_sitemap  = '\uf0e8';
  public static final char icon_umbrella  = '\uf0e9';
  public static final char icon_paste = '\uf0ea';
  public static final char icon_clipboard  = '\uf0ea';
  public static final char icon_lightbulb_o  = '\uf0eb';
  public static final char icon_exchange  = '\uf0ec';
  public static final char icon_cloud_download  = '\uf0ed';
  public static final char icon_cloud_upload  = '\uf0ee';
  public static final char icon_user_md  = '\uf0f0';
  public static final char icon_stethoscope  = '\uf0f1';
  public static final char icon_suitcase  = '\uf0f2';
  public static final char icon_bell_o  = '\uf0a2';
  public static final char icon_coffee  = '\uf0f4';
  public static final char icon_cutlery  = '\uf0f5';
  public static final char icon_file_text_o  = '\uf0f6';
  public static final char icon_building_o  = '\uf0f7';
  public static final char icon_hospital_o  = '\uf0f8';
  public static final char icon_ambulance  = '\uf0f9';
  public static final char icon_medkit  = '\uf0fa';
  public static final char icon_fighter_jet  = '\uf0fb';
  public static final char icon_beer  = '\uf0fc';
  public static final char icon_h_square  = '\uf0fd';
  public static final char icon_plus_square  = '\uf0fe';
  public static final char icon_angle_double_left  = '\uf100';
  public static final char icon_angle_double_right  = '\uf101';
  public static final char icon_angle_double_up  = '\uf102';
  public static final char icon_angle_double_down  = '\uf103';
  public static final char icon_angle_left  = '\uf104';
  public static final char icon_angle_right  = '\uf105';
  public static final char icon_angle_up  = '\uf106';
  public static final char icon_angle_down  = '\uf107';
  public static final char icon_desktop  = '\uf108';
  public static final char icon_laptop  = '\uf109';
  public static final char icon_tablet  = '\uf10a';
  public static final char icon_mobile_phone = '\uf10b';
  public static final char icon_mobile  = '\uf10b';
  public static final char icon_circle_o  = '\uf10c';
  public static final char icon_quote_left  = '\uf10d';
  public static final char icon_quote_right  = '\uf10e';
  public static final char icon_spinner  = '\uf110';
  public static final char icon_circle  = '\uf111';
  public static final char icon_mail_reply = '\uf112';
  public static final char icon_reply  = '\uf112';
  public static final char icon_github_alt  = '\uf113';
  public static final char icon_folder_o  = '\uf114';
  public static final char icon_folder_open_o  = '\uf115';
  public static final char icon_smile_o  = '\uf118';
  public static final char icon_frown_o  = '\uf119';
  public static final char icon_meh_o  = '\uf11a';
  public static final char icon_gamepad  = '\uf11b';
  public static final char icon_keyboard_o  = '\uf11c';
  public static final char icon_flag_o  = '\uf11d';
  public static final char icon_flag_checkered = '\uf11e';
  public static final char icon_terminal = '\uf120';
  public static final char icon_code = '\uf121';
  public static final char icon_reply_all = '\uf122';
  public static final char icon_mail_reply_all = '\uf122';
  public static final char icon_star_half_o  = '\uf123';
  public static final char icon_star_half_empty  = '\uf123';
  public static final char icon_star_half_full  = '\uf123';
  public static final char icon_location_arrow  = '\uf124';
  public static final char icon_crop  = '\uf125';
  public static final char icon_code_fork  = '\uf126';
  public static final char icon_unlink = '\uf127';
  public static final char icon_chain_broken  = '\uf127';
  public static final char icon_question  = '\uf128';
  public static final char icon_info  = '\uf129';
  public static final char icon_exclamation  = '\uf12a';
  public static final char icon_superscript  = '\uf12b';
  public static final char icon_subscript  = '\uf12c';
  public static final char icon_eraser  = '\uf12d';
  public static final char icon_puzzle_piece  = '\uf12e';
  public static final char icon_microphone  = '\uf130';
  public static final char icon_microphone_slash  = '\uf131';
  public static final char icon_shield  = '\uf132';
  public static final char icon_calendar_o  = '\uf133';
  public static final char icon_fire_extinguisher  = '\uf134';
  public static final char icon_rocket  = '\uf135';
  public static final char icon_maxcdn  = '\uf136';
  public static final char icon_chevron_circle_left  = '\uf137';
  public static final char icon_chevron_circle_right  = '\uf138';
  public static final char icon_chevron_circle_up  = '\uf139';
  public static final char icon_chevron_circle_down  = '\uf13a';
  public static final char icon_html5  = '\uf13b';
  public static final char icon_css3  = '\uf13c';
  public static final char icon_anchor  = '\uf13d';
  public static final char icon_unlock_alt  = '\uf13e';
  public static final char icon_bullseye  = '\uf140';
  public static final char icon_ellipsis_h  = '\uf141';
  public static final char icon_ellipsis_v  = '\uf142';
  public static final char icon_rss_square  = '\uf143';
  public static final char icon_play_circle  = '\uf144';
  public static final char icon_ticket  = '\uf145';
  public static final char icon_minus_square  = '\uf146';
  public static final char icon_minus_square_o  = '\uf147';
  public static final char icon_level_up  = '\uf148';
  public static final char icon_level_down  = '\uf149';
  public static final char icon_check_square  = '\uf14a';
  public static final char icon_pencil_square  = '\uf14b';
  public static final char icon_external_link_square  = '\uf14c';
  public static final char icon_share_square  = '\uf14d';
  public static final char icon_compass  = '\uf14e';
  public static final char icon_toggle_down = '\uf150';
  public static final char icon_caret_square_o_down  = '\uf150';
  public static final char icon_toggle_up = '\uf151';
  public static final char icon_caret_square_o_up  = '\uf151';
  public static final char icon_toggle_right = '\uf152';
  public static final char icon_caret_square_o_right  = '\uf152';
  public static final char icon_euro = '\uf153';
  public static final char icon_eur  = '\uf153';
  public static final char icon_gbp  = '\uf154';
  public static final char icon_dollar = '\uf155';
  public static final char icon_usd  = '\uf155';
  public static final char icon_rupee = '\uf156';
  public static final char icon_inr  = '\uf156';
  public static final char icon_cny = '\uf157';
  public static final char icon_rmb = '\uf157';
  public static final char icon_yen = '\uf157';
  public static final char icon_jpy  = '\uf157';
  public static final char icon_ruble = '\uf158';
  public static final char icon_rouble = '\uf158';
  public static final char icon_rub  = '\uf158';
  public static final char icon_won = '\uf159';
  public static final char icon_krw  = '\uf159';
  public static final char icon_bitcoin = '\uf15a';
  public static final char icon_btc  = '\uf15a';
  public static final char icon_file  = '\uf15b';
  public static final char icon_file_text  = '\uf15c';
  public static final char icon_sort_alpha_asc  = '\uf15d';
  public static final char icon_sort_alpha_desc  = '\uf15e';
  public static final char icon_sort_amount_asc  = '\uf160';
  public static final char icon_sort_amount_desc  = '\uf161';
  public static final char icon_sort_numeric_asc  = '\uf162';
  public static final char icon_sort_numeric_desc  = '\uf163';
  public static final char icon_thumbs_up  = '\uf164';
  public static final char icon_thumbs_down  = '\uf165';
  public static final char icon_youtube_square  = '\uf166';
  public static final char icon_youtube  = '\uf167';
  public static final char icon_xing  = '\uf168';
  public static final char icon_xing_square  = '\uf169';
  public static final char icon_youtube_play  = '\uf16a';
  public static final char icon_dropbox  = '\uf16b';
  public static final char icon_stack_overflow  = '\uf16c';
  public static final char icon_instagram  = '\uf16d';
  public static final char icon_flickr  = '\uf16e';
  public static final char icon_adn  = '\uf170';
  public static final char icon_bitbucket  = '\uf171';
  public static final char icon_bitbucket_square  = '\uf172';
  public static final char icon_tumblr  = '\uf173';
  public static final char icon_tumblr_square  = '\uf174';
  public static final char icon_long_arrow_down  = '\uf175';
  public static final char icon_long_arrow_up  = '\uf176';
  public static final char icon_long_arrow_left  = '\uf177';
  public static final char icon_long_arrow_right  = '\uf178';
  public static final char icon_apple  = '\uf179';
  public static final char icon_windows  = '\uf17a';
  public static final char icon_android  = '\uf17b';
  public static final char icon_linux  = '\uf17c';
  public static final char icon_dribbble  = '\uf17d';
  public static final char icon_skype  = '\uf17e';
  public static final char icon_foursquare  = '\uf180';
  public static final char icon_trello  = '\uf181';
  public static final char icon_female  = '\uf182';
  public static final char icon_male  = '\uf183';
  public static final char icon_gittip  = '\uf184';
  public static final char icon_sun_o  = '\uf185';
  public static final char icon_moon_o  = '\uf186';
  public static final char icon_archive  = '\uf187';
  public static final char icon_bug  = '\uf188';
  public static final char icon_vk  = '\uf189';
  public static final char icon_weibo  = '\uf18a';
  public static final char icon_renren  = '\uf18b';
  public static final char icon_pagelines  = '\uf18c';
  public static final char icon_stack_exchange  = '\uf18d';
  public static final char icon_arrow_circle_o_right  = '\uf18e';
  public static final char icon_arrow_circle_o_left  = '\uf190';
  public static final char icon_toggle_left = '\uf191';
  public static final char icon_caret_square_o_left  = '\uf191';
  public static final char icon_dot_circle_o  = '\uf192';
  public static final char icon_wheelchair  = '\uf193';
  public static final char icon_vimeo_square  = '\uf194';
  public static final char icon_turkish_lira = '\uf195';
  public static final char icon_try  = '\uf195';
  public static final char icon_plus_square_o  = '\uf196';
}
