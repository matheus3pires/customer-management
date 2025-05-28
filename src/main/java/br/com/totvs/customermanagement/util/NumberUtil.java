package br.com.totvs.customermanagement.util;

public class NumberUtil {

    /**
     * Validates a Brazilian CPF (Cadastro de Pessoas Físicas) number.
     * <p>
     * This method removes any non-numeric characters and then checks if the CPF:
     * <ul>
     *   <li>Has exactly 11 digits</li>
     *   <li>Is not composed of the same digit repeated (e.g., 00000000000)</li>
     *   <li>Has valid verifying digits according to the official algorithm</li>
     * </ul>
     * </p>
     *
     * @param cpf the CPF number as a {@link String}, with or without formatting (e.g., "123.456.789-09" or "12345678909")
     * @return {@code true} if the CPF is valid; {@code false} otherwise
     */
    public static boolean isValidCpf(String cpf) {
        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }
        int firstVerifierDigit = 11 - (sum % 11);
        if (firstVerifierDigit > 9) {
            firstVerifierDigit = 0;
        }

        if ((cpf.charAt(9) - '0') != firstVerifierDigit) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        int secondVerifierDigit = 11 - (sum % 11);
        if (secondVerifierDigit > 9) {
            secondVerifierDigit = 0;
        }

        return (cpf.charAt(10) - '0') == secondVerifierDigit;
    }

}
