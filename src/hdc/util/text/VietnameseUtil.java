package hdc.util.text;

public class VietnameseUtil {
  
  final static public boolean containVietnameseCharacter(String src){
    if (src == null || src.isEmpty()) return false ;
    int length = src.length() ;
    for(int i = 0; i < length; i++){
      char c = src.charAt(i) ;
      if(c <= 'z') continue ;
      if(isVietnameseCharacter(c)) return true;
    }
    return false ;
  }

  final static public boolean isVietnameseCharacter(char c) {
    return removeVietnameseAccent(c) != c ;
  }
  
  final static public String removeVietnameseAccent(String src) {
    if(src == null || src.isEmpty()) return null ;
    StringBuilder b = new StringBuilder() ;
    int length = src.length() ;
    for(int i = 0; i < length; i++) {
      char c = src.charAt(i) ;
      b.append(removeVietnameseAccent(c)) ;
    }
    return b.toString() ;
  }
  
  final static public char toLowcaseVietnamese(char c) {
    if(c <= 'z') return c ;
    switch (c) {
      case 'À': return 'à';
      case 'Á': return 'á';
      case 'Ả': return 'ả';
      case 'Ã': return 'ã';
      case 'Ạ': return 'ạ';
      case 'Ă': return 'ă';
      case 'Ằ': return 'ằ';
      case 'Ắ': return 'ắ';
      case 'Ẳ': return 'ẳ';
      case 'Ẵ': return 'ẵ';
      case 'Ặ': return 'ặ';
      case 'Â': return 'â';
      case 'Ầ': return 'ầ';
      case 'Ấ': return 'ấ';
      case 'Ẩ': return 'ẩ';
      case 'Ẫ': return 'ẫ';
      case 'Ậ': return 'ậ';
      case 'Đ': return 'đ';
      case 'È': return 'è';
      case 'É': return 'é';
      case 'Ẻ': return 'ẻ';
      case 'Ẽ': return 'ẽ';
      case 'Ẹ': return 'ẹ';
      case 'Ê': return 'ê';
      case 'Ề': return 'ề';
      case 'Ế': return 'ế';
      case 'Ể': return 'ể';
      case 'Ễ': return 'ễ';
      case 'Ệ': return 'ệ';
      case 'Ì': return 'ì';
      case 'Í': return 'í';
      case 'Ỉ': return 'ỉ';
      case 'Ĩ': return 'ĩ';
      case 'Ị': return 'ị';
      case 'Ò': return 'ò';
      case 'Ó': return 'ó';
      case 'Ỏ': return 'ỏ';
      case 'Õ': return 'õ';
      case 'Ọ': return 'ọ';
      case 'Ô': return 'ô';
      case 'Ồ': return 'ồ';
      case 'Ố': return 'ố';
      case 'Ổ': return 'ổ';
      case 'Ỗ': return 'ỗ';
      case 'Ộ': return 'ộ';
      case 'Ơ': return 'ơ';
      case 'Ờ': return 'ờ';
      case 'Ớ': return 'ớ';
      case 'Ở': return 'ở';
      case 'Ỡ': return 'ỡ';
      case 'Ợ': return 'ợ';
      case 'Ù': return 'ù';
      case 'Ú': return 'ú';
      case 'Ủ': return 'ủ';
      case 'Ũ': return 'ũ';
      case 'Ụ': return 'ụ';
      case 'Ư': return 'ư';
      case 'Ừ': return 'ừ';
      case 'Ứ': return 'ứ';
      case 'Ử': return 'ử';
      case 'Ữ': return 'ữ';
      case 'Ự': return 'ự';
      case 'Ỳ': return 'ỳ';
      case 'Ý': return 'ý';
      case 'Ỷ': return 'ỷ';
      case 'Ỹ': return 'ỹ';
      case 'Ỵ': return 'ỵ';
      default: return c;
    }
  }

  final static public char removeVietnameseAccent(char c){
    if(c <= 'z') return c ;
    switch (c) {
      case 'À': return 'A';
      case 'à': return 'a';
      case 'Á': return 'A';
      case 'á': return 'a';
      case 'Ả': return 'A';
      case 'ả': return 'a';
      case 'Ã': return 'A';
      case 'ã': return 'a';
      case 'Ạ': return 'A';
      case 'ạ': return 'a';
      case 'Ă': return 'A';
      case 'ă': return 'a';
      case 'Ằ': return 'A';
      case 'ằ': return 'a';
      case 'Ắ': return 'A';
      case 'ắ': return 'a';
      case 'Ẳ': return 'A';
      case 'ẳ': return 'a';
      case 'Ẵ': return 'A';
      case 'ẵ': return 'a';
      case 'Ặ': return 'A';
      case 'ặ': return 'a';
      case 'Â': return 'A';
      case 'â': return 'a';
      case 'Ầ': return 'A';
      case 'ầ': return 'a';
      case 'Ấ': return 'A';
      case 'ấ': return 'a';
      case 'Ẩ': return 'A';
      case 'ẩ': return 'a';
      case 'Ẫ': return 'A';
      case 'ẫ': return 'a';
      case 'Ậ': return 'A';
      case 'ậ': return 'a';
      case 'Đ': return 'D';
      case 'đ': return 'd';
      case 'È': return 'E';
      case 'è': return 'e';
      case 'É': return 'E';
      case 'é': return 'e';
      case 'Ẻ': return 'E';
      case 'ẻ': return 'e';
      case 'Ẽ': return 'E';
      case 'ẽ': return 'e';
      case 'Ẹ': return 'E';
      case 'ẹ': return 'e';
      case 'Ê': return 'E';
      case 'ê': return 'e';
      case 'Ề': return 'E';
      case 'ề': return 'e';
      case 'Ế': return 'E';
      case 'ế': return 'e';
      case 'Ể': return 'E';
      case 'ể': return 'e';
      case 'Ễ': return 'E';
      case 'ễ': return 'e';
      case 'Ệ': return 'E';
      case 'ệ': return 'e';
      case 'Ì': return 'I';
      case 'ì': return 'i';
      case 'Í': return 'I';
      case 'í': return 'i';
      case 'Ỉ': return 'I';
      case 'ỉ': return 'i';
      case 'Ĩ': return 'I';
      case 'ĩ': return 'i';
      case 'Ị': return 'I';
      case 'ị': return 'i';
      case 'Ò': return 'O';
      case 'ò': return 'o';
      case 'Ó': return 'O';
      case 'ó': return 'o';
      case 'Ỏ': return 'O';
      case 'ỏ': return 'o';
      case 'Õ': return 'O';
      case 'õ': return 'o';
      case 'Ọ': return 'O';
      case 'ọ': return 'o';
      case 'Ô': return 'O';
      case 'ô': return 'o';
      case 'Ồ': return 'O';
      case 'ồ': return 'o';
      case 'Ố': return 'O';
      case 'ố': return 'o';
      case 'Ổ': return 'O';
      case 'ổ': return 'o';
      case 'Ỗ': return 'O';
      case 'ỗ': return 'o';
      case 'Ộ': return 'O';
      case 'ộ': return 'o';
      case 'Ơ': return 'O';
      case 'ơ': return 'o';
      case 'Ờ': return 'O';
      case 'ờ': return 'o';
      case 'Ớ': return 'O';
      case 'ớ': return 'o';
      case 'Ở': return 'O';
      case 'ở': return 'o';
      case 'Ỡ': return 'O';
      case 'ỡ': return 'o';
      case 'Ợ': return 'O';
      case 'ợ': return 'o';
      case 'Ù': return 'U';
      case 'ù': return 'u';
      case 'Ú': return 'U';
      case 'ú': return 'u';
      case 'Ủ': return 'U';
      case 'ủ': return 'u';
      case 'Ũ': return 'U';
      case 'ũ': return 'u';
      case 'Ụ': return 'U';
      case 'ụ': return 'u';
      case 'Ư': return 'U';
      case 'ư': return 'u';
      case 'Ừ': return 'U';
      case 'ừ': return 'u';
      case 'Ứ': return 'U';
      case 'ứ': return 'u';
      case 'Ử': return 'U';
      case 'ử': return 'u';
      case 'Ữ': return 'U';
      case 'ữ': return 'u';
      case 'Ự': return 'U';
      case 'ự': return 'u';
      case 'Ỳ': return 'Y';
      case 'ỳ': return 'y';
      case 'Ý': return 'Y';
      case 'ý': return 'y';
      case 'Ỷ': return 'Y';
      case 'ỷ': return 'y';
      case 'Ỹ': return 'Y';
      case 'ỹ': return 'y';
      case 'Ỵ': return 'Y';
      case 'ỵ': return 'y';
      default: return c;
    }
  }
}
