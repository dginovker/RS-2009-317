using DatabaseLibrary;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace NpcDropEditor
{
    public partial class frmMain : Form
    {
        public frmMain()
        {
            InitializeComponent();
        }

        private void frmMain_Load(object sender, EventArgs e)
        {
            
        }

		private void label1_Click(object sender, EventArgs e)
		{

		}

        private void btnSearch_Click(object sender, EventArgs e)
        {
            Console.WriteLine(Connect.GetDatabase().ToString());
        }
    }
}