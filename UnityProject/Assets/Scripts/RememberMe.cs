using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Security.Cryptography;
using System.Net;
using System.Windows.Forms;

namespace Catan {
    public class rememberMe { 

        Resolute_Launcher mainForm;
        public rememberMe(Catan mainForm) {
            this.mainForm = mainForm;
        }

        private static readonly byte[] LastLoginSalt = new byte[] { 0x0c, 0x9d, 0x4a, 0xe4, 0x1e, 0x83, 0x15, 0xfc };
        private const string LastLoginPassword = "Passwords";

        static String rootPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), "/.resolute/");
        static String lastLoginPath = Path.Combine(rootPath, "lastlogin");

        public String[] GetLastLogin() {
            try {
                byte[] encryptedLogin = File.ReadAllBytes(lastLoginPath);
                PKCSKeyGenerator crypto = new PKCSKeyGenerator(LastLoginPassword, LastLoginSalt, 5, 1);
                ICryptoTransform cryptoTransform = crypto.Decryptor;
                byte[] decrypted = cryptoTransform.TransformFinalBlock(encryptedLogin, 0, encryptedLogin.Length);
                short userLength = IPAddress.HostToNetworkOrder(BitConverter.ToInt16(decrypted, 0));
                byte[] user = decrypted.Skip(2).Take(userLength).ToArray();
                short passLength = IPAddress.HostToNetworkOrder(BitConverter.ToInt16(decrypted, userLength + 2));
                byte[] password = decrypted.Skip(4 + userLength).ToArray();
                String[] result = new String[2];
                result[0] = System.Text.Encoding.UTF8.GetString(user);
                result[1] = System.Text.Encoding.UTF8.GetString(password);
                return result;
            }
            catch (Exception e){
                MessageBox.Show(e.StackTrace);
                return null;
            }
        }

        public void SetLastLogin(String username, String userpassword) {
            byte[] decrypted = BitConverter.GetBytes(IPAddress.NetworkToHostOrder((short)username.Length))
                .Concat(System.Text.Encoding.UTF8.GetBytes(username))
                .Concat(BitConverter.GetBytes(IPAddress.NetworkToHostOrder((short)password.Length)))
                .Concat(System.Text.Encoding.UTF8.GetBytes(userpassword)).ToArray();

            PKCSKeyGenerator crypto = new PKCSKeyGenerator(LastLoginPassword, LastLoginSalt, 5, 1);
            ICryptoTransform cryptoTransform = crypto.Encryptor;
            byte[] encrypted = cryptoTransform.TransformFinalBlock(decrypted, 0, decrypted.Length);
            if (File.Exists(lastLoginPath))
                File.Delete(lastLoginPath);
            using (Stream stream = File.Create(lastLoginPath))
                stream.Write(encrypted, 0, encrypted.Length);
        }

        //rememberMe functions

        public void save(String username, String userpassword) {
/**/            if (mainForm.rememberBox.Checked) {
                    SetLastLogin(username, userpassword);

                String settings;
/**/                if (mainForm.updateButton.Checked) {
                    settings = "1";
                }
                else {
                    settings = "0";
                }

/**/                if (mainForm.consoleButton.Checked) {
                    settings += "1";
                }
                else {
                    settings += "0";
                }

/**/                if (mainForm.snapshotButton.Checked) {
                    settings += "1";
                }
                else {
                    settings += "0";
                }

                if (File.Exists(Path.Combine(rootPath, "rememberMe.txt"))) {
                    File.Delete(Path.Combine(rootPath, "rememberMe.txt"));
                }
                using (StreamWriter sw = File.CreateText(Path.Combine(rootPath, "rememberMe.txt"))) {
                    sw.WriteLine(settings);
                    sw.Close();
                }
            }
        }

        public void remember() {
            if (File.Exists(Path.Combine(rootPath, "rememberMe.txt"))) {
                if (File.Exists(Path.Combine(rootPath, "lastlogin"))) {
                    String[] loginInfo = GetLastLogin();
/**/                    mainForm.userText.Text = loginInfo[0];
/**/                    mainForm.passText.Text = loginInfo[1];
                }
                String settings;
                using (StreamReader sr = File.OpenText(Path.Combine(rootPath, "rememberMe.txt"))) {
                    settings = sr.ReadLine();
                    sr.Close();
                }

                Char[] settingArray = settings.ToCharArray();
                byte i = 0;
                while (i < 3) {
                    switch (i) {
                        case 0:
                            if (settingArray[0] == '1') {
/**/                                mainForm.updateButton.Checked = true;
                            }
                            else {
/**/                                mainForm.updateButton.Checked = false;
                            }
                            break;
                        case 1:
                            if (settingArray[1] == '1') {
/**/                                mainForm.consoleButton.Checked = true;
                            }
                            else {
/**/                                mainForm.consoleButton.Checked = false;
                            }
                            break;
                        case 2:
                            if (settingArray[2] == '1') {
/**/                                mainForm.snapshotButton.Checked = true;
                            }
                            else {
/**/                                mainForm.normalButton.Checked = true;
                            }
                            break;
                    }
                    i++;
                }

/**/                mainForm.rememberBox.Checked = true;
            }
        }


    }
}
