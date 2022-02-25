public class Arguments {

    String loginEndpoint = "https://www.salesforce.com";
    String username = null;
    String password = null;
    String appVersion = "55.0";
    String quoteId = null;
    String paymentGatewayId = null;
    String paymentMethodId = null;

    public Arguments(String application, String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                if ("-host".equals(args[i]) || "-h".equals(args[i])) {
                    loginEndpoint = args[i + 1];
                } else if ("-user".equals(args[i]) || "-u".equals(args[i])) {
                    username = args[i + 1];
                } else if ("-password".equals(args[i]) || "-p".equals(args[i])) {
                    password = args[i + 1];
                } else if ("-version".equals(args[i]) || "-v".equals(args[i])) {
                    appVersion = args[i + 1];
                } else if ("-quote".equals(args[i]) || "-q".equals(args[i])) {
                    quoteId = args[i + 1];
                } else if ("-gateway".equals(args[i]) || "-g".equals(args[i])) {
                    paymentGatewayId = args[i + 1];
                } else if ("-method".equals(args[i]) || "-m".equals(args[i])) {
                    paymentMethodId = args[i + 1];
                }
            }
            if (username == null || password == null || paymentGatewayId == null || paymentMethodId == null) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.out.println("Usage: " + application + " [-host <login endpoint>] -user <username> -password <password> [-version <api version>] -quote <quote id> -gateway <payment gateway id> -method <payment method id>");
            System.exit(1);
        }
    }
}
