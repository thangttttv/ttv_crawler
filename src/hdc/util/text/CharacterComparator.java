package hdc.util.text;


public interface CharacterComparator {
  final static CharacterComparator DEFAULT = new DefaultCharacterComparator() ;
  final static CharacterComparator NO_VN_ACCENT = new NoVietnameseAccentCharacterComparator() ;
  
  public int compare(char c1, char c2) ;
  
  static public class DefaultCharacterComparator implements CharacterComparator {
    public int compare(char c1, char c2) {
      c1 = Character.toLowerCase(c1) ;
      c2 = Character.toLowerCase(c2) ;
      return c1 - c2;
    }
  }
  
  static public class NoVietnameseAccentCharacterComparator implements CharacterComparator {
    public int compare(char c1, char c2) {
      c1 = VietnameseUtil.removeVietnameseAccent(Character.toLowerCase(c1)) ;
      c2 = VietnameseUtil.removeVietnameseAccent(Character.toLowerCase(c2)) ;
      return c1 - c2;
    }
  }
}
