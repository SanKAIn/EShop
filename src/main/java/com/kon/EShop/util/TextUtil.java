package com.kon.EShop.util;

public class TextUtil {
   public static String removeSpaces(String text) {
      int spaceCount = 0;
      for (char c : text.toCharArray()) {
         if (c == ' ') {
            spaceCount++;
         } else {
            if (spaceCount > 1) {
               text = removeSpaces(text.replace(" ".repeat(spaceCount), " "));
            }else spaceCount = 0;
         }
      }
      return text;
   }
}
