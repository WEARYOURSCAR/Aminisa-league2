/**
 * Vercel Serverless Function: api/welcome.js
 * Sends a welcome email to ASCL registered players using Resend API.
 */

module.exports = async (req, res) => {
  // Set CORS headers for utility in development and preview environments
  res.setHeader('Access-Control-Allow-Credentials', 'true');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET,OPTIONS,PATCH,DELETE,POST,PUT');
  res.setHeader(
    'Access-Control-Allow-Headers',
    'X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version'
  );

  // Handle preflight requests
  if (req.method === 'OPTIONS') {
    res.status(200).end();
    return;
  }

  // Only permit POST requests
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed. Use POST.' });
  }

  const { email, fullName, uniquePlayerId } = req.body;

  // Validate fields
  if (!email || !fullName) {
    return res.status(400).json({ error: 'Missing registration details (email or fullName)' });
  }

  const resendApiKey = process.env.RESEND_API_KEY;
  if (!resendApiKey) {
    return res.status(500).json({ 
      error: 'Resend API Key is missing on the server. Please define RESEND_API_KEY in your environment variables.' 
    });
  }

  // Determine sender: if domain verified, it should be changed. Defaulting to Resend testing sender.
  const senderEmail = process.env.SENDER_EMAIL || 'ASCL <onboarding@resend.dev>';

  try {
    const response = await fetch('https://api.resend.com/emails', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${resendApiKey}`
      },
      body: JSON.stringify({
        from: senderEmail,
        to: [email],
        subject: 'Welcome to Aminisa Sport Club League (ASCL)!',
        html: `
          <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0A0A0A; color: #ffffff; padding: 30px; border-radius: 12px; max-width: 600px; margin: 40px auto; border: 1.5.dp solid #D4AF37; box-shadow: 0 10px 30px rgba(0,0,0,0.5);">
            <div style="text-align: center; margin-bottom: 25px;">
              <h1 style="color: #00A651; font-weight: 900; margin: 0; font-size: 24px; letter-spacing: 1px; text-transform: uppercase;">
                Aminisa Sport Club League
              </h1>
              <p style="color: #D4AF37; font-size: 11px; font-weight: bold; letter-spacing: 2px; margin: 5px 0 0 0; text-transform: uppercase;">
                Official Player Roster
              </p>
            </div>
            
            <hr style="border: 0; border-top: 1px solid rgba(212, 175, 55, 0.25); margin: 20px 0;"/>
            
            <p style="font-size: 15px; line-height: 1.6; color: #e0e0e0;">
              Hello <strong>${fullName}</strong>,
            </p>
            
            <p style="font-size: 14px; line-height: 1.6; color: #cccccc;">
              Welcome to the <strong>Aminisa Sport Club League (ASCL) Season 1</strong> in Ilorin. 
              We have successfully received your roster envelope submission. Your application details are currently being audited by league officials.
            </p>
            
            <div style="background-color: #141414; border: 1px solid rgba(0, 166, 81, 0.4); padding: 20px; border-radius: 8px; text-align: center; margin: 25px 0;">
              <span style="display: block; font-size: 10px; color: #888888; font-weight: bold; letter-spacing: 1.5px; text-transform: uppercase; margin-bottom: 6px;">
                YOUR ATHLETE IDENTIFIER (ASC-ID)
              </span>
              <span style="font-size: 22px; font-weight: 900; color: #D4AF37; font-family: monospace; letter-spacing: 1px;">
                ${uniquePlayerId}
              </span>
            </div>
            
            <p style="font-size: 13px; line-height: 1.6; color: #aaaaaa;">
              <strong>Roster Status:</strong> <span style="color: #D4AF37; font-weight: bold; text-transform: uppercase;">Pending Evaluation</span>
            </p>
            
            <p style="font-size: 13px; line-height: 1.6; color: #aaaaaa;">
              Our evaluation panel reviews incoming submissions within 60 minutes. Please make sure you have tapped the <strong>WhatsApp Share to Admin</strong> button inside your dashboard to send your verification token and payment receipt proof to fast-track your approval.
            </p>
            
            <hr style="border: 0; border-top: 1px solid rgba(255,255,255,0.05); margin: 25px 0;"/>
            
            <div style="text-align: center; font-size: 11px; color: #666666;">
              <p style="margin: 0;">This is an automated operational notification regarding your registration.</p>
              <p style="margin: 5px 0 0 0; color: #00A651; font-weight: bold;">ASCL • Mix & Mingle Palms Mall, Ilorin, Nigeria</p>
            </div>
          </div>
        `
      })
    });

    const responseData = await response.json();
    if (!response.ok) {
      return res.status(response.status).json({ 
        error: responseData.message || 'Call to Resend API failed.' 
      });
    }

    return res.status(200).json({ success: true, id: responseData.id });
  } catch (error) {
    return res.status(500).json({ error: error.message });
  }
};
