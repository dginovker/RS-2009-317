namespace NpcDropEditor
{
    partial class frmMain
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.labelTitle = new System.Windows.Forms.Label();
            this.labelSearchInstruction = new System.Windows.Forms.Label();
            this.txtSearchNpcs = new System.Windows.Forms.RichTextBox();
            this.btnSearch = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // labelTitle
            // 
            this.labelTitle.AccessibleName = "labelTitle";
            this.labelTitle.AutoSize = true;
            this.labelTitle.Font = new System.Drawing.Font("Microsoft Sans Serif", 14F);
            this.labelTitle.Location = new System.Drawing.Point(155, 11);
            this.labelTitle.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.labelTitle.Name = "labelTitle";
            this.labelTitle.Size = new System.Drawing.Size(227, 29);
            this.labelTitle.TabIndex = 0;
            this.labelTitle.Text = "Gielinor Drop Editor";
            this.labelTitle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.labelTitle.Click += new System.EventHandler(this.label1_Click);
            // 
            // labelSearchInstruction
            // 
            this.labelSearchInstruction.AccessibleName = "labelSearchInstruction";
            this.labelSearchInstruction.AutoSize = true;
            this.labelSearchInstruction.Location = new System.Drawing.Point(152, 41);
            this.labelSearchInstruction.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.labelSearchInstruction.Name = "labelSearchInstruction";
            this.labelSearchInstruction.Size = new System.Drawing.Size(245, 17);
            this.labelSearchInstruction.TabIndex = 1;
            this.labelSearchInstruction.Text = "Start by searching for an NPC\'s name";
            // 
            // txtSearchNpcs
            // 
            this.txtSearchNpcs.AccessibleName = "txtNpcSearch";
            this.txtSearchNpcs.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
            this.txtSearchNpcs.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
            this.txtSearchNpcs.ForeColor = System.Drawing.Color.Gray;
            this.txtSearchNpcs.Location = new System.Drawing.Point(93, 68);
            this.txtSearchNpcs.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.txtSearchNpcs.Name = "txtSearchNpcs";
            this.txtSearchNpcs.Size = new System.Drawing.Size(359, 30);
            this.txtSearchNpcs.TabIndex = 2;
            this.txtSearchNpcs.Text = "Search...";
            // 
            // btnSearch
            // 
            this.btnSearch.AccessibleName = "btnSearch";
            this.btnSearch.Location = new System.Drawing.Point(199, 112);
            this.btnSearch.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.btnSearch.Name = "btnSearch";
            this.btnSearch.Size = new System.Drawing.Size(141, 43);
            this.btnSearch.TabIndex = 3;
            this.btnSearch.Text = "Search";
            this.btnSearch.UseVisualStyleBackColor = true;
            this.btnSearch.Click += new System.EventHandler(this.btnSearch_Click);
            // 
            // frmMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(539, 167);
            this.Controls.Add(this.btnSearch);
            this.Controls.Add(this.txtSearchNpcs);
            this.Controls.Add(this.labelSearchInstruction);
            this.Controls.Add(this.labelTitle);
            this.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.Name = "frmMain";
            this.Text = "Npc Drop Editor";
            this.Load += new System.EventHandler(this.frmMain_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

		#endregion

		private System.Windows.Forms.Label labelTitle;
		private System.Windows.Forms.Label labelSearchInstruction;
		private System.Windows.Forms.RichTextBox txtSearchNpcs;
		private System.Windows.Forms.Button btnSearch;
	}
}